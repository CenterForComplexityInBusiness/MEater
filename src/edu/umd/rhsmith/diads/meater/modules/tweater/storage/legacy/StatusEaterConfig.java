package edu.umd.rhsmith.diads.meater.modules.tweater.storage.legacy;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesEligible;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertyTypes;
import edu.umd.rhsmith.diads.meater.modules.tweater.UserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;

@SetupPropertiesEligible
public abstract class StatusEaterConfig extends ComponentConfig implements
		StatusEaterInitializer {

	public StatusEaterConfig() {
		super();

		this.registerMediaProcessorName("", UserStatusData.class);
		this.registerMediaProcessorName(StatusEater.PNAME_QADD, QueryItem.class);
		this.registerMediaProcessorName(StatusEater.PNAME_QRMV, QueryItem.class);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	private static final String CKEY_DISCARDS = "discardsUnmatched";
	@SetupProperty(propertyType = SetupPropertyTypes.BOOLEAN,
			uiName = "Discard unmatched statuses",
			uiDescription = "Whether or not to discard incloming statuses that do not match any recived QueryItems")
	private boolean discardsUnmatched;

	public boolean isDiscardsUnmatched() {
		return this.discardsUnmatched;
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	@Override
	public void resetConfiguration() {
		this.discardsUnmatched = false;
	}

	@Override
	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.loadConfigurationPropertiesFrom(config);
		this.discardsUnmatched = config.getBoolean(CKEY_DISCARDS,
				this.discardsUnmatched);
	}

	@Override
	protected void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.saveConfigurationPropertiesTo(config);
		config.setProperty(CKEY_DISCARDS, this.discardsUnmatched);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

}
