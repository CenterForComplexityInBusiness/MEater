package edu.umd.rhsmith.diads.meater.core.app.components;

import java.util.HashMap;
import java.util.Map;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.MEaterInitializer;
import edu.umd.rhsmith.diads.meater.core.app.MEaterMain;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaManager;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaSource;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.meater.util.ControlUnit;

public class ComponentManager extends ControlUnit {

	private final MEaterMain main;
	private final Map<String, Component> components;
	private final MediaManager mediaManager;

	public ComponentManager(MEaterInitializer init, MEaterMain main) {
		this.main = main;

		// MEaterGeneralSettings settings = config.getGeneralSettings();

		// component registrations
		this.components = new HashMap<String, Component>();

		// media
		this.mediaManager = new MediaManager(init, main);

		// logging
		this.setLogger(this.main.getLogger());
		this.setLogName("ComponentManager");
	}

	/*
	 * --------------------------------
	 * General getters/setters
	 * --------------------------------
	 */

	public MEaterMain getMain() {
		return main;
	}

	public final MediaManager getMediaManager() {
		return mediaManager;
	}

	/*
	 * --------------------------------
	 * Component registration
	 * --------------------------------
	 */

	public void registerComponent(Component component)
			throws IllegalStateException {
		synchronized (this.controlLock) {
			this.requireUnStarted();

			this.logInfo(MSG_REG_FMT, component.getName());
			this.components.put(component.getName(), component);
			component.setComponentManager(this);

			for (MediaSource<?> source : component.getMediaSources()) {
				this.mediaManager.registerSource(component.getName(), source);
			}
			for (MediaProcessor<?> processor : component.getMediaProcessors()) {
				this.mediaManager.registerProcessor(component.getName(),
						processor);
			}
		}
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	@Override
	protected void doStartupRoutine() throws ControlException {
		try {
			for (Component c : this.components.values()) {
				c.initialize();
			}
		} catch (MEaterConfigurationException e) {
			throw new ControlException(MSG_ERR_INIT_CONFIG, e);
		}

		this.mediaManager.start();

		for (Component c : this.components.values()) {
			c.start();
		}
	}

	@Override
	protected void doShutdownRoutine() {
		for (Component c : this.components.values()) {
			c.stop();
		}

		this.mediaManager.stop();
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_REG_FMT = "Component %s registered";
	private static final String MSG_ERR_INIT_CONFIG = "Unable to start due to incorrectly-configured component";

}
