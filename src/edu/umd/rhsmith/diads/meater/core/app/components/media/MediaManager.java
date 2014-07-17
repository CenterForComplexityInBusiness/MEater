package edu.umd.rhsmith.diads.meater.core.app.components.media;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.umd.rhsmith.diads.meater.core.app.MEaterInitializer;
import edu.umd.rhsmith.diads.meater.core.app.MEaterMain;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.meater.util.ControlUnit;
import edu.umd.rhsmith.diads.meater.util.Util;

public class MediaManager extends ControlUnit {

	private final MEaterMain main;

	private final Map<String, MediaProcessor<?>> processors;
	private final Map<String, MediaSource<?>> sources;
	private final Map<MediaSource<?>, Collection<MediaProcessor<?>>> outputs;
	private final Map<MediaSource<?>, Collection<MediaProcessor<?>>> rejectable;

	private final ThreadPoolExecutor processingThreadPool;
	private final double rejectionThreshold;

	public MediaManager(MEaterInitializer init, MEaterMain main) {
		this.main = main;

		// media registrations
		this.processors = new HashMap<String, MediaProcessor<?>>();
		this.sources = new HashMap<String, MediaSource<?>>();
		this.outputs = new HashMap<MediaSource<?>, Collection<MediaProcessor<?>>>();
		this.rejectable = new HashMap<MediaSource<?>, Collection<MediaProcessor<?>>>();

		// create thread pool
		this.processingThreadPool = new ThreadPoolExecutor(init
				.getCoreThreadPoolSize(), init.getMaxThreadPoolSize(), init
				.getThreadPoolKeepAliveTimeS(), TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		this.rejectionThreshold = init.getResourceLimitRejectionThreshold();

		// logging
		this.setLogger(this.main.getLogger());
		this.setLogName(this.main.getLogName());
	}

	public <M> void submitMedia(M media, MediaSource<M> source) {
		// check that we're under utilization; otherwise, we may reject
		// certain outputs
		boolean reject = false;
		double utilization = Util.getMemoryUtilizationFraction();
		if (utilization >= rejectionThreshold) {
			this.logFine(MSG_THRESHOLD_REJECTED_FMT, utilization,
					this.rejectionThreshold, media.toString());
			reject = true;
		}

		// send the media to all outputs as their own tasks
		final M m = media;
		for (MediaProcessor<?> processor : this.outputs.get(source)) {
			// ...unless we're allowed to reject it
			if (reject && rejectable.get(source).contains(processor)) {
				continue;
			}

			// we already checked that things were okay when the output mapping
			// was registered, so we can suppress this warning
			@SuppressWarnings("unchecked")
			final MediaProcessor<? super M> p = (MediaProcessor<? super M>) processor;
			this.processingThreadPool.submit(new Runnable() {
				@Override
				public void run() {
					p.processMedia(m);
				}
			});
		}
	}

	/*
	 * --------------------------------
	 * General getters/setters
	 * --------------------------------
	 */

	public MEaterMain getMain() {
		return main;
	}

	/*
	 * --------------------------------
	 * Media handler registration
	 * --------------------------------
	 */

	public <M> void registerSource(String ownerName, MediaSource<M> source)
			throws IllegalStateException {
		synchronized (this.controlLock) {
			this.requireUnStarted();
			this.logInfo(MSG_SRC_REG_FMT, source.getSourceName());
			this.sources.put(Media.handlerName(ownerName, source
					.getSourceName()), source);
			source.setMediaManager(this);
		}
	}

	public <M> void registerProcessor(String ownerName,
			MediaProcessor<M> processor) throws IllegalStateException {
		synchronized (this.controlLock) {
			this.requireUnStarted();
			this.logInfo(MSG_PROC_REG_FMT, processor.getProcessorName());
			this.processors.put(Media.handlerName(ownerName, processor
					.getProcessorName()), processor);
		}
	}

	public <M> void registerSource(MediaSource<M> source)
			throws IllegalStateException {
		this.registerSource(null, source);
	}

	public <M> void registerProcessor(MediaProcessor<M> processor)
			throws IllegalStateException {
		this.registerProcessor(null, processor);
	}

	public <M> void registerOutput(MediaSource<? extends M> source,
			MediaProcessor<? super M> output, boolean rejectable)
			throws IllegalStateException {
		synchronized (this.controlLock) {
			this.requireUnStarted();

			this.logInfo(MSG_OUTPUT_REG_FMT, source.getSourceName(), output
					.getProcessorName());
			Collection<MediaProcessor<?>> ps;
			ps = this.outputs.get(output);
			if (ps == null) {
				ps = new LinkedList<MediaProcessor<?>>();
				this.outputs.put(source, ps);
			}
			ps.add(output);

			if (rejectable) {
				Collection<MediaProcessor<?>> rj;
				rj = this.outputs.get(output);
				if (rj == null) {
					rj = new HashSet<MediaProcessor<?>>();
					this.rejectable.put(source, rj);
				}
				rj.add(output);
			}
		}
	}

	// we must cast generic parameters based on compatibility of media classes -
	// the compiler can't check this, but we do.
	@SuppressWarnings("unchecked")
	public <M> MediaSource<? extends M> getMediaSource(String ownerName,
			String sourceName, Class<? extends M> mediaclass)
			throws NoSuchElementException {
		String name = Media.handlerName(ownerName, sourceName);

		MediaSource<?> source = this.sources.get(name);
		if (source == null) {
			throw new NoSuchElementException(this.messageString(
					MSG_ERR_NO_SRC_FMT, name));
		}

		if (mediaclass.isAssignableFrom(source.getMediaClass())) {
			return (MediaSource<? extends M>) source;
		} else {
			throw new NoSuchElementException(this.messageString(
					MSG_ERR_INCOMPATIBLE_SRC_FMT, name));
		}
	}

	public <M> MediaSource<? extends M> getMediaSource(String sourceName,
			Class<? extends M> mediaclass) throws NoSuchElementException {
		return this.getMediaSource(null, sourceName, mediaclass);
	}

	// we must cast generic parameters based on compatibility of media classes -
	// the compiler can't check this, but we do.
	@SuppressWarnings("unchecked")
	public <M> MediaProcessor<? super M> getMediaProcessor(String ownerName,
			String processorName, Class<? extends M> mediaclass)
			throws NoSuchElementException {
		String name = Media.handlerName(ownerName, processorName);

		MediaProcessor<?> processor = this.processors.get(name);
		if (processor == null) {
			throw new NoSuchElementException(this.messageString(
					MSG_ERR_NO_PROC_FMT, name));
		}

		if (processor.getMediaClass().isAssignableFrom(mediaclass)) {
			return (MediaProcessor<? super M>) processor;
		} else {
			throw new NoSuchElementException(this.messageString(
					MSG_ERR_INCOMPATIBLE_PROC_FMT, name));
		}
	}

	public <M> MediaProcessor<? super M> getMediaProcessor(
			String processorName, Class<? extends M> mediaclass)
			throws NoSuchElementException {
		return this.getMediaProcessor(null, processorName, mediaclass);
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	@Override
	protected void doStartupRoutine() throws ControlException {
	}

	@Override
	protected void doShutdownRoutine() {
		try {
			this.stopThreadPool();
		} catch (InterruptedException e) {
			this.logWarning(MSG_WARN_POOL_INTERRUPTED);
		}
	}

	/*
	 * --------------------------------
	 * Thread pool
	 * --------------------------------
	 */

	private void stopThreadPool() throws InterruptedException {
		this.logInfo(MSG_ENDING_POOL);
		this.processingThreadPool.shutdown();
		this.processingThreadPool.awaitTermination(Long.MAX_VALUE,
				TimeUnit.SECONDS);
		this.logInfo(MSG_POOL_TERMINATED);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_OUTPUT_REG_FMT = "Output %s -> %s registered";
	private static final String MSG_PROC_REG_FMT = "Media processor %s registered";
	private static final String MSG_SRC_REG_FMT = "Media source %s registered";
	private static final String MSG_POOL_TERMINATED = "Media processor thread-pool terminated";
	private static final String MSG_ENDING_POOL = "Awaiting termination of media processor thread-pool";
	private static final String MSG_WARN_POOL_INTERRUPTED = "Interrupted while awaiting termination of media processor thread-pool";

	private static final String MSG_ERR_INCOMPATIBLE_SRC_FMT = "Media source %s uses media class %s, which is incompatible with requested type %s";
	private static final String MSG_ERR_NO_SRC_FMT = "No media source exists with name %s";
	private static final String MSG_ERR_INCOMPATIBLE_PROC_FMT = "Media processor %s uses media class %s, which is incompatible with requested type %s";
	private static final String MSG_ERR_NO_PROC_FMT = "No media processor exists with name %s";

	private static final String MSG_THRESHOLD_REJECTED_FMT = "Memory utilization at %d >= %d; media %s rejected";

}
