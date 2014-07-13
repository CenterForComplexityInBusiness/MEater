package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

import edu.umd.rhsmith.diads.meater.core.app.components.media.BaseMediaProcessor;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;

public abstract class BaseMediaSetFilter<M> implements MediaSetFilter<M> {

	private final MediaProcessor<M> present;
	private final MediaProcessor<M> absent;

	public BaseMediaSetFilter(String presentName, String absentName,
			Class<M> mediaClass) {
		this.present = new BaseMediaProcessor<M>(presentName, mediaClass) {
			@Override
			public boolean processMedia(M media) {
				return contains(media);
			}
		};
		this.absent = new BaseMediaProcessor<M>(absentName, mediaClass) {
			@Override
			public boolean processMedia(M media) {
				return !contains(media);
			}
		};
	}

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
