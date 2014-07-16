package edu.umd.rhsmith.diads.meater.modules.common.sentiment;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.props.StringProperty;

public class PySentimentAnalyzerConfig extends ComponentConfig implements
		PySentimentAnalyzerInitializer {

	public PySentimentAnalyzerConfig() {
		super();
		this.registerMediaProcessorName("", SentimentAnalyzable.class);
		this.registerConfigProperty(classiferFilename);
		this.registerConfigProperty(featuresFilename);
	}

	@Override
	public PySentimentAnalyzer instantiateComponent()
			throws MEaterConfigurationException {
		return new PySentimentAnalyzer(this);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	// analyzer properties

	private static final String CKEY_FEATURES_FILENAME = "features";
	private static final String DEFAULT_FEATURES_FILENAME = "features.pickle";
	private static final String UINAME_FEATURES_FILENAME = "Feature-set filename";
	private static final String UIDESC_FEATURES_FILENAME = "Filename of the serialized feature-set which the sentiment analyzer will load";
	private final StringProperty featuresFilename = new StringProperty(
			CKEY_FEATURES_FILENAME, DEFAULT_FEATURES_FILENAME,
			UINAME_FEATURES_FILENAME, UIDESC_FEATURES_FILENAME);
	private static final String CKEY_CLASSIFIER_FILENAME = "classifer";
	private static final String DEFAULT_CLASSIFIER_FILENAME = "classifier.pickle";
	private static final String UINAME_CLASSIFIER_FILENAME = "Classifier filename";
	private static final String UIDESC_CLASSIFIER_FILENAME = "Filename of the serialized classifier which the sentiment analyzer will load";
	private final StringProperty classiferFilename = new StringProperty(
			CKEY_CLASSIFIER_FILENAME, DEFAULT_CLASSIFIER_FILENAME,
			UINAME_CLASSIFIER_FILENAME, UIDESC_CLASSIFIER_FILENAME);

	public String getClassifierFilename() {
		return this.classiferFilename.getVal();
	}

	public String getFeaturesFilename() {
		return this.featuresFilename.getVal();
	}

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

	public static final String TNAME = "PySentimentAnalyzer";
	public static final String TDESC = "A Python-based tool for performing sentiment analysis on media instances implementing the SentimentAnalyzable interface.\n"
			+ "Allows configuration of the serialized classifer and feature sets.";

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

}
