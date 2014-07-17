package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

import edu.umd.rhsmith.diads.meater.core.app.components.media.BaseMediaProcessor;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;

public abstract class BaseMediaSetUpdater<M> implements MediaSetUpdater<M> {

	private final MediaProcessor<M> adder;
	private final MediaProcessor<M> remover;

	public BaseMediaSetUpdater(String adderName, String removerName,
			Class<M> mediaClass) throws IllegalArgumentException {
		this.adder = new BaseMediaProcessor<M>(adderName, mediaClass) {
			@Override
			public boolean processMedia(M media) {
				return add(media);
			}
		};
		this.remover = new BaseMediaProcessor<M>(removerName, mediaClass) {
			@Override
			public boolean processMedia(M media) {
				return remove(media);
			}
		};
	}

	public abstract boolean add(M media);

	public abstract boolean remove(M media);

	@Override
	public MediaProcessor<M> getMediaAdder() {
		return adder;
	}

	@Override
	public MediaProcessor<M> getMediaRemover() {
		return remover;
	}
}
