package edu.umd.rhsmith.diads.meater.core.config.components.media;

public class MediaRegistration<M> {

	private final Class<M> mediaClass;
	private final String name;
	private final String description;

	public MediaRegistration(Class<M> mediaClass, String name,
			String description) {
		this.mediaClass = mediaClass;
		this.name = name;
		this.description = description;
	}

	public MediaRegistration(Class<M> mediaClass, String name) {
		this(mediaClass, name, "");
	}

	public MediaRegistration(Class<M> mediaClass) {
		this(mediaClass, mediaClass.getSimpleName());
	}

	public final Class<M> getMediaClass() {
		return this.mediaClass;
	}

	public final String getMediaName() {
		return this.name;
	}

	public final String getMediaDescription() {
		return this.description;
	}
}
