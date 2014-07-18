package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;

public abstract class BaseMediaSetFilter<M> implements MediaSetFilter<M> {

	private final MediaProcessor<M> present;
	private final MediaProcessor<M> absent;

	public BaseMediaSetFilter() throws IllegalArgumentException {
		this.present = new MediaProcessor<M>() {
			@Override
			public boolean processMedia(M media) {
				return contains(media);
			}

			@Override
			public Class<M> getMediaClass() {
				return BaseMediaSetFilter.this.getMediaClass();
			}

			@Override
			public String getProcessorName() {
				return BaseMediaSetFilter.this.getFilterPresentName();
			}
		};
		this.absent = new MediaProcessor<M>() {
			@Override
			public boolean processMedia(M media) {
				return !contains(media);
			}

			@Override
			public Class<M> getMediaClass() {
				return BaseMediaSetFilter.this.getMediaClass();
			}

			@Override
			public String getProcessorName() {
				return BaseMediaSetFilter.this.getFilterAbsentName();
			}
		};
	}

	public abstract String getFilterPresentName();

	public abstract String getFilterAbsentName();

	public abstract Class<M> getMediaClass();

	public abstract boolean contains(M media);

	@Override
	public MediaProcessor<M> getFilterPresentMedia() {
		return present;
	}

	@Override
	public MediaProcessor<M> getFilterAbsentMedia() {
		return absent;
	}
}
