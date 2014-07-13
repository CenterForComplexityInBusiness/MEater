package edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentManager;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesEligible;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertyTypes;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QuerySourceConfig;

@SetupPropertiesEligible
public class CsvQuerySourceConfig extends QuerySourceConfig implements
		CsvQuerySourceInitializer {

	public static final String TNAME = "CsvQuerySource-legacy";
	public static final String TDESC = "A (legacy) component which builds query-sets by periodically checking a local csv file.";

	public CsvQuerySourceConfig() {
		super();
	}

	@Override
	public Component instantiateComponent(ComponentManager mgr)
			throws MEaterConfigurationException {
		return new CsvQuerySource(this, mgr);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	private static final String CKEY_FILENAME = "filename";
	private static final String DEFAULT_FILENAME = "queries.csv";

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
		super.resetConfiguration();
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
