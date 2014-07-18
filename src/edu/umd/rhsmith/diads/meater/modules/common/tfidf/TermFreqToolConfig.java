package edu.umd.rhsmith.diads.meater.modules.common.tfidf;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.props.IntProperty;

public class TermFreqToolConfig extends ComponentConfig implements
		TermFreqToolInitializer {

	public TermFreqToolConfig() {
		super();
		this.registerMediaProcessorName("", TermExtractable.class);
		this.registerConfigProperty(nGrams);
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


	private static final String UIDESC_NGRAMS = "Value of n for n-gram generation (set <= 1 to disable n-grams)";
	private static final String UINAME_NGRAMS = "N-gram count";
	private static final int DEFAULT_NGRAMS = 1;
	private static final String CKEY_NGRAMS = "nGrams";
	private final IntProperty nGrams = new IntProperty(CKEY_NGRAMS,
			DEFAULT_NGRAMS, UINAME_NGRAMS, UIDESC_NGRAMS);

	@Override
	public int getNGrams() {
		return this.nGrams.getVal();
	}

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
