package edu.umd.rhsmith.diads.meater.modules.tweater.storage.legacy;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesEligible;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertyTypes;

@SetupPropertiesEligible
public class CsvStatusEaterConfig extends StatusEaterConfig implements
		CsvStatusEaterInitializer {

	public static final String TNAME = "CSVStatusEater-legacy";
	public static final String TDESC = "A (legacy) component which persists UserStatusData objects (information about both statuses and the users that posted them) to a local csv file.";

	public CsvStatusEaterConfig() {
		super();
	}

	@Override
	public Component instantiateComponent()
			throws IllegalStateException, MEaterConfigurationException {
		return new CsvStatusEater(this);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	public static final String CKEY_FILENAME = "filename";
	public static final String DEFAULT_FILENAME = "results.csv";
	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "input filename")
	private String filename;

	public String getFilename() {
		return this.filename;
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
	 * Config operations
	 * --------------------------------
	 */

	@Override
	public void resetConfiguration() {
		this.filename = DEFAULT_FILENAME;
	}

	@Override
	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.loadConfigurationPropertiesFrom(config);
		this.filename = config.getString(CKEY_FILENAME, this.filename);
	}

	@Override
	protected void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.saveConfigurationPropertiesTo(config);
		config.setProperty(CKEY_FILENAME, this.filename);
	}
}
