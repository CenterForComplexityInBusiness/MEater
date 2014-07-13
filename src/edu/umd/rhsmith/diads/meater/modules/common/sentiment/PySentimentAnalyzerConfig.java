package edu.umd.rhsmith.diads.meater.modules.common.sentiment;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesEligible;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertyTypes;

@SetupPropertiesEligible
public class PySentimentAnalyzerConfig extends ComponentConfig implements
		PySentimentAnalyzerInitializer {

	// values taken from TwEater
	private static final String CKEY_FEATURES_FILENAME = "features";
	private static final String DEFAULT_FEATURES_FILENAME = "features.pickle";
	private static final String CKEY_CLASSIFIER_FILENAME = "classifer";
	private static final String DEFAULT_CLASSIFIER_FILENAME = "classifier.pickle";

	public PySentimentAnalyzerConfig() {
		super();

		this.registerMediaProcessorName("", SentimentAnalyzable.class);
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
	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "Feature-set filename",
			uiDescription = "Filename of the serialized feature-set which the sentiment analyzer will load")
	private String featuresFilename;
	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "Classifier filename",
			uiDescription = "Filename of the serialized classifier which the sentiment analyzer will load")
	private String classiferFilename;

	public String getClassifierFilename() {
		return this.classiferFilename;
	}

	public String getFeaturesFilename() {
		return this.featuresFilename;
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	@Override
	public void resetConfiguration() {
		this.classiferFilename = DEFAULT_CLASSIFIER_FILENAME;
		this.featuresFilename = DEFAULT_FEATURES_FILENAME;
	}

	@Override
	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.loadConfigurationPropertiesFrom(config);

		this.classiferFilename = config.getString(CKEY_CLASSIFIER_FILENAME,
				this.classiferFilename);
		this.featuresFilename = config.getString(CKEY_FEATURES_FILENAME,
				this.classiferFilename);
	}

	@Override
	protected void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.saveConfigurationPropertiesTo(config);

		config.setProperty(CKEY_CLASSIFIER_FILENAME, this.classiferFilename);
		config.setProperty(CKEY_FEATURES_FILENAME, this.featuresFilename);
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

	public static final String TNAME = "PySentimentAnalyzer";
	public static final String TDESC = "A Python-based tool for performing sentiment analysis on media instances implementing the SentimentAnalyzable interface.\n"
			+ "Allows configuration of the serialized classifer and feature sets.";

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

}
