package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaSource;

public interface MediaSetUpdateViewer<M> {
	public MediaSource<M> getAddedMedia();

	public MediaSource<M> getRemovedMedia();
}
