package edu.umd.rhsmith.diads.meater.core.app.components.media;

public abstract class BaseMediaProcessor<M> implements MediaProcessor<M> {

	private String name;
	private Class<M> mediaClass;

	public BaseMediaProcessor(String name, Class<M> mediaClass) {
		this.name = name;
		this.mediaClass = mediaClass;
	}

	@Override
	public Class<M> getMediaClass() {
		return this.mediaClass;
	}

	@Override
	public String getProcessorName() {
		return this.name;
	}

}
