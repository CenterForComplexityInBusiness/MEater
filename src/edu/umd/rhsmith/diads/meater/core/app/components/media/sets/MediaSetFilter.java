package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;

public interface MediaSetFilter<M> {
	public MediaProcessor<M> getFilterPresentMedia();

	public MediaProcessor<M> getFilterAbsentMedia();
}
