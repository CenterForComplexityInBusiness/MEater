package edu.umd.rhsmith.diads.meater.core.app.components.media;

public class MediaSource<M> {
	private Class<? extends M> mediaClass;
	private final String name;
	private MediaManager mediaManager;

	public MediaSource(String name, Class<? extends M> mediaClass)
			throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException(MSG_ERR_NULL_NAME);
		}
		if (mediaClass == null) {
			throw new IllegalArgumentException(MSG_ERR_NULL_CLASS);
		}

		this.name = name;
		this.mediaClass = mediaClass;
	}

	public final String getSourceName() {
		return this.name;
	}

	void setMediaManager(MediaManager mediaManager) {
		this.mediaManager = mediaManager;
	}

	public MediaManager getMediaManager() {
		return this.mediaManager;
	}

	public final Class<? extends M> getMediaClass() {
		return this.mediaClass;
	}

	public final void setMediaClass(Class<? extends M> mediaClass)
			throws IllegalArgumentException, IllegalStateException {
		if (mediaClass == null) {
			throw new IllegalArgumentException(MSG_ERR_NULL_CLASS);
		}
		if (this.mediaManager != null) {
			throw new IllegalStateException(MSG_ERR_CHANGED);
		}

		this.mediaClass = mediaClass;
	}

	public final void sourceMedia(M media) throws NullPointerException {
		this.mediaManager.submitMedia(media, this);
	}

	@Override
	public final String toString() {
		return String.format("Media source %s", this.getSourceName());
	}

	private static final String MSG_ERR_NULL_CLASS = "Media source media classes must be non-null";
	private static final String MSG_ERR_NULL_NAME = "Media source names must be non-null";
	private static final String MSG_ERR_CHANGED = "Cannot change media class after having been registered to a MediaManager";

}
