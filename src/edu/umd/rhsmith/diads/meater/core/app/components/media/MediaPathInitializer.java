package edu.umd.rhsmith.diads.meater.core.app.components.media;

import java.util.Collection;

import edu.umd.rhsmith.diads.meater.core.app.components.ComponentInitializer;

public interface MediaPathInitializer<M> extends ComponentInitializer {
	public Class<M> getMediaClass();
	public Collection<String> getSourceNames();
	public Collection<String> getProcessorNames();
	public boolean isRejectable();
}
