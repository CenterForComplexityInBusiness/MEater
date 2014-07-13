package edu.umd.rhsmith.diads.meater.modules.common;

import edu.umd.rhsmith.diads.meater.core.config.ConfigModule;
import edu.umd.rhsmith.diads.meater.modules.common.sentiment.SentimentAnalyzable;
import edu.umd.rhsmith.diads.meater.modules.common.sentiment.PySentimentAnalyzerConfig;

public class MEaterCommon extends ConfigModule {

	public MEaterCommon() {
		super("Common");

		this.registerComponentType(PySentimentAnalyzerConfig.class);
		this.registerMediaType(SentimentAnalyzable.class);
	}

	@Override
	public String getUiName() {
		return "MEater common component module";
	}

	@Override
	public String getUiDescription() {
		return "A module containing shared component types applicable to many kinds of media.";
	}
}
