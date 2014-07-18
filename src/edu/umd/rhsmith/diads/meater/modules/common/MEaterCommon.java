package edu.umd.rhsmith.diads.meater.modules.common;

import edu.umd.rhsmith.diads.meater.core.config.ConfigModule;
import edu.umd.rhsmith.diads.meater.modules.common.sentiment.SentimentAnalyzable;
import edu.umd.rhsmith.diads.meater.modules.common.sentiment.PySentimentToolConfig;
import edu.umd.rhsmith.diads.meater.modules.common.tfidf.TermExtractable;
import edu.umd.rhsmith.diads.meater.modules.common.tfidf.TermFreqToolConfig;

public class MEaterCommon extends ConfigModule {

	public MEaterCommon() {
		super("Common");

		this.registerConfigType(PySentimentToolConfig.class);
		this.registerMediaType(SentimentAnalyzable.class);
		
		this.registerConfigType(TermFreqToolConfig.class);
		this.registerMediaType(TermExtractable.class);
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
