package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;

public abstract class BaseMediaSetUpdater<M> implements MediaSetUpdater<M> {

	private final MediaProcessor<M> adder;
	private final MediaProcessor<M> remover;

	public BaseMediaSetUpdater() throws IllegalArgumentException {
		this.adder = new MediaProcessor<M>() {
			@Override
			public boolean processMedia(M media) {
				return add(media);
			}

			@Override
			public Class<M> getMediaClass() {
				return BaseMediaSetUpdater.this.getMediaClass();
			}

			@Override
			public String getProcessorName() {
				return BaseMediaSetUpdater.this.getAdderName();
			}
		};
		this.remover = new MediaProcessor<M>() {
			@Override
			public boolean processMedia(M media) {
				return remove(media);
			}

			@Override
			public Class<M> getMediaClass() {
				return BaseMediaSetUpdater.this.getMediaClass();
			}

			@Override
			public String getProcessorName() {
				return BaseMediaSetUpdater.this.getRemoverName();
			}
		};
	}

	public abstract String getAdderName();

	public abstract String getRemoverName();

	public abstract Class<M> getMediaClass();

	@Override
	public abstract boolean add(M media);

	@Override
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
