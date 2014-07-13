package edu.umd.rhsmith.diads.meater.core.app.components.media;

public interface MediaProcessor<M> {
	public Class<M> getMediaClass();

	public String getProcessorName();

	public boolean processMedia(M media);
}
