package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;

public interface MediaSetUpdater<M> {
	public MediaProcessor<M> getMediaAdder();

	public MediaProcessor<M> getMediaRemover();
}
