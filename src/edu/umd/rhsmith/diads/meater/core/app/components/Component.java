package edu.umd.rhsmith.diads.meater.core.app.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaManager;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaSource;
import edu.umd.rhsmith.diads.meater.util.ControlUnit;

public abstract class Component extends ControlUnit {

	private final String name;

	private ComponentManager componentManager;

	private final Object initWaiter;
	private boolean initBegun;
	private boolean initFinished;

	private final Set<MediaSource<?>> mediaSources;
	private final Set<MediaProcessor<?>> mediaProcessors;

	public Component(ComponentInitializer init, ComponentManager componentEnv)
			throws MEaterConfigurationException {
		super();

		this.name = init.getInstanceName();
		this.setLogName(this.name);

		this.initWaiter = new Object();
		this.initBegun = false;
		this.initFinished = false;

		this.mediaSources = new HashSet<MediaSource<?>>();
		this.mediaProcessors = new HashSet<MediaProcessor<?>>();
		this.setupExpectedMedia(init);

		componentEnv.registerComponent(this);
	}

	/*
	 * --------------------------------
	 * General getters/setters
	 * --------------------------------
	 */

	public final String getName() {
		return this.name;
	}

	public ComponentManager getComponentManager() {
		return componentManager;
	}

	public MediaManager getMediaManager() {
		if (this.componentManager == null) {
			return null;
		} else {
			return this.componentManager.getMediaManager();
		}
	}

	final void setComponentManager(ComponentManager mgr)
			throws IllegalStateException {
		this.requireUnStarted();
		this.componentManager = mgr;
		this.setLogger(mgr.getLogger());
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	public final void initialize() throws MEaterConfigurationException {
		this.requireUnInited();

		synchronized (this.initWaiter) {
			this.initBegun = true;
			this.getLogger().info(this.messageString(MSG_INITING));
			this.initWaiter.notifyAll();
		}

		this.requireInited();

		this.doInitRoutine();

		// check that we have the media nodes that we expected
		this.verifyExpectedMedia();

		synchronized (this.initWaiter) {
			this.initFinished = true;
			this.getLogger().info(this.messageString(MSG_INITED));
			this.initWaiter.notifyAll();
		}
	}

	protected abstract void doInitRoutine() throws MEaterConfigurationException;

	/*
	 * --------------------------------
	 * Control status-checkers
	 * --------------------------------
	 */

	public final boolean isInitBegun() {
		return initBegun;
	}

	public final boolean isInitFinished() {
		return initFinished;
	}

	/*
	 * --------------------------------
	 * Control state-validators
	 * --------------------------------
	 */

	public final void requireInited() throws IllegalStateException {
		if (!this.initFinished) {
			throw new IllegalStateException(this.messageString(MSG_ERR_NOSTART));
		}
	}

	public final void requireUnInited() throws IllegalStateException {
		if (this.initBegun) {
			throw new IllegalStateException(this.messageString(MSG_ERR_START));
		}
	}

	/*
	 * --------------------------------
	 * Control status-waiters
	 * --------------------------------
	 */

	public final void awaitInitBegun() throws InterruptedException {
		synchronized (this.initWaiter) {
			while (!this.initBegun) {
				this.initWaiter.wait();
			}
		}
	}

	public final void awaitInitFinished() throws InterruptedException {
		synchronized (this.initWaiter) {
			while (!this.initFinished) {
				this.initWaiter.wait();
			}
		}
	}

	/*
	 * --------------------------------
	 * Media interaction
	 * --------------------------------
	 */

	public Set<MediaSource<?>> getMediaSources() {
		return new HashSet<MediaSource<?>>(this.mediaSources);
	}

	protected void registerMediaSource(MediaSource<?> source)
			throws MEaterConfigurationException {
		this.mediaSources.add(source);
	}

	public Set<MediaProcessor<?>> getMediaProcessors() {
		return new HashSet<MediaProcessor<?>>(this.mediaProcessors);
	}

	protected void registerMediaProcessor(MediaProcessor<?> processor)
			throws MEaterConfigurationException {
		this.mediaProcessors.add(processor);
	}

	/*
	 * --------------------------------
	 * Media validation
	 * --------------------------------
	 */

	private Map<String, Class<?>> expectedSources;
	private Map<String, Class<?>> expectedProcessors;

	private void setupExpectedMedia(ComponentInitializer init) {
		this.expectedProcessors = new HashMap<String, Class<?>>();
		this.expectedSources = new HashMap<String, Class<?>>();
	}

	public final void expectMediaSource(String name, Class<?> mediaClass) {
		this.expectedSources.put(name, mediaClass);
	}

	public final void expectMediaProcessor(String name, Class<?> mediaClass) {
		this.expectedProcessors.put(name, mediaClass);
	}

	private void validateMediaSource(MediaSource<?> source)
			throws MEaterConfigurationException {
		Class<?> expectedMediaClass = this.expectedSources.remove(source
				.getSourceName());

		if (expectedMediaClass == null) {
			return;
		}

		if (!expectedMediaClass.isAssignableFrom(source.getMediaClass())) {
			throw new IllegalStateException(this.messageString(
					MSG_ERR_SRC_INCOMPATIBLE_FMT, source.getSourceName(),
					source.getMediaClass(), expectedMediaClass));
		}
	}

	private void validateMediaProcessor(MediaProcessor<?> processor)
			throws MEaterConfigurationException {
		Class<?> expectedMediaClass = this.expectedProcessors.remove(processor
				.getProcessorName());

		if (expectedMediaClass == null) {
			return;
		}

		if (!expectedMediaClass.isAssignableFrom(processor.getMediaClass())) {
			throw new IllegalStateException(this.messageString(
					MSG_ERR_PROC_INCOMPATIBLE_FMT,
					processor.getProcessorName(), processor.getMediaClass(),
					expectedMediaClass));
		}
	}

	private void verifyExpectedMedia() throws MEaterConfigurationException {
		StringBuilder error = new StringBuilder();
		boolean missing = false;

		for (MediaProcessor<?> processor : this.mediaProcessors) {
			this.validateMediaProcessor(processor);
		}
		for (MediaSource<?> source : this.mediaSources) {
			this.validateMediaSource(source);
		}

		for (String name : this.expectedSources.keySet()) {
			error.append(name);
			error.append("\n");
			missing = true;
		}
		for (String name : this.expectedProcessors.keySet()) {
			error.append(name);
			error.append("\n");
			missing = true;
		}
		if (missing) {
			throw new MEaterConfigurationException(this.messageString(
					MSG_ERR_MISSING_EXPECTED, error.toString()));
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_MISSING_EXPECTED = "Expected media sources / processors were not registered --\n%s";
	private static final String MSG_ERR_PROC_INCOMPATIBLE_FMT = "Media processor '%s' has media class %s incompatible with declared class %s";
	private static final String MSG_ERR_SRC_INCOMPATIBLE_FMT = "Media source '%s' has media class %s incompatible with declared class %s";

	private static final String MSG_ERR_NOSTART = "not yet initialized";
	private static final String MSG_ERR_START = "already initialized";
	private static final String MSG_INITING = "Component initializing";
	private static final String MSG_INITED = "Component initializing";
}
