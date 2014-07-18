package edu.umd.rhsmith.diads.meater.modules.common.tfidf;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;

public class TermFreqToolConfig extends ComponentConfig implements
		TermFreqToolInitializer {

	public TermFreqToolConfig() {
		super();
		this.registerMediaProcessorName("", TermExtractable.class);
	}

	@Override
	public TermFreqTool instantiateComponent()
			throws MEaterConfigurationException {
		return new TermFreqTool(this);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	/*
	 * --------------------------------
	 * UI
	 * --------------------------------
	 */

	@Override
	public String getUiDescription() {
		return TDESC;
	}

	/*
	 * --------------------------------
	 * Config type registration
	 * --------------------------------
	 */

	public static final String TNAME = "TermFreqTool";
	public static final String TDESC = "A tool to extract term frequencies from media text for future use, such as for TF-IDF analysis.";

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

}
