package edu.umd.rhsmith.diads.meater.core.app.components.media;

import java.util.TreeMap;

public abstract class BaseMediaProcessor<M> implements MediaProcessor<M> {

	private String name;
	private Class<M> mediaClass;

	public BaseMediaProcessor(String name, Class<M> mediaClass)
			throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException(MSG_ERR_NULL_NAME);
		}
		if (mediaClass == null) {
			throw new IllegalArgumentException(MSG_ERR_NULL_CLASS);
		}

		new TreeMap<String, String>();

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

	private static final String MSG_ERR_NULL_CLASS = "Media processor media classes must be non-null";
	private static final String MSG_ERR_NULL_NAME = "Media processor names must be non-null";

}
