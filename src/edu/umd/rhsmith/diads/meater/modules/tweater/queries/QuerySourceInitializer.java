package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import edu.umd.rhsmith.diads.meater.core.app.components.ComponentInitializer;

public interface QuerySourceInitializer extends ComponentInitializer {
	public long getRebuildIntervalMs();
}
