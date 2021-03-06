package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaSource;

public class SimpleMediaSetUpdateViewer<M> implements MediaSetUpdateViewer<M> {

	private final MediaSource<M> adder;
	private final MediaSource<M> remover;

	public SimpleMediaSetUpdateViewer(String adderName, String removerName,
			Class<M> mediaClass) throws IllegalArgumentException {
		this.adder = new MediaSource<M>(removerName, mediaClass);
		this.remover = new MediaSource<M>(removerName, mediaClass);
	}

	public void add(M media) {
		this.adder.sourceMedia(media);
	}

	public void remove(M media) {
		this.remover.sourceMedia(media);
	}

	@Override
	public MediaSource<M> getAddedMedia() {
		return adder;
	}

	@Override
	public MediaSource<M> getRemovedMedia() {
		return remover;
	}
}
