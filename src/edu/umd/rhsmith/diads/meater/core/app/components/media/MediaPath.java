package edu.umd.rhsmith.diads.meater.core.app.components.media;

import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.util.ControlException;

public final class MediaPath<M> extends Component implements MediaProcessor<M> {

	private final Class<M> mediaClass;

	private final Collection<String> sourceNames;
	private final Collection<String> processorNames;
	private final Collection<MediaProcessor<? super M>> processors;
	private final boolean rejectable;

	public MediaPath(MediaPathInitializer<M> init)
			throws MEaterConfigurationException {
		super(init);
		this.mediaClass = init.getMediaClass();
		this.registerMediaProcessor(this);
		this.sourceNames = init.getSourceNames();
		this.processorNames = init.getProcessorNames();
		this.processors = new LinkedList<MediaProcessor<? super M>>();
		this.rejectable = init.isRejectable();
	}

	@Override
	public String getProcessorName() {
		return "";
	}

	@Override
	public Class<M> getMediaClass() {
		return this.mediaClass;
	}

	public boolean isRejectable() {
		return rejectable;
	}

	@Override
	public boolean processMedia(M media) {
		for (MediaProcessor<? super M> processor : this.processors) {
			if (!processor.processMedia(media)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
		MediaManager mediaManager = this.getComponentManager()
				.getMediaManager();
		try {
			for (String sourceName : this.sourceNames) {
				MediaSource<? extends M> s = mediaManager.getMediaSource(
						sourceName, this.getMediaClass());
				mediaManager.registerOutput(s, this, this.rejectable);
			}
			for (String processorName : this.processorNames) {
				this.processors.add(mediaManager.getMediaProcessor(
						processorName, this.getMediaClass()));
			}
		} catch (NoSuchElementException e) {
			throw new MEaterConfigurationException(e.getMessage());
		}
	}

	@Override
	protected void doStartupRoutine() throws ControlException {
	}

	@Override
	protected void doShutdownRoutine() {
	}
}
