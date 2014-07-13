package edu.umd.rhsmith.diads.meater.modules.common.sentiment;

import edu.umd.rhsmith.diads.meater.core.app.components.ComponentInitializer;

public interface PySentimentAnalyzerInitializer extends ComponentInitializer {

	public String getClassifierFilename();

	public String getFeaturesFilename();

}
