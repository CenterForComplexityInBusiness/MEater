package edu.umd.rhsmith.diads.meater.modules.tweater.tlc;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesEligible;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertyTypes;
import edu.umd.rhsmith.diads.meater.modules.tweater.UserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryFollow;

@SetupPropertiesEligible
public class TimelineCollectorConfig extends ComponentConfig implements
		TimelineCollectorInitializer {

	public TimelineCollectorConfig() {
		super();
		this.registerMediaSourceName(TimelineCollector.SRCNAME_TWEETS,
				UserStatusData.class);
		this.registerMediaProcessorName(TimelineCollector.PNAME_USERS,
				QueryFollow.class);
	}

	@Override
	public Component instantiateComponent()
			throws MEaterConfigurationException {
		return new TimelineCollector(this);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	private static final String CKEY_OAUTH = "oAuthName";
	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "OAuth Configuration file",
			uiDescription = "The name of the XML file containing the Twitter OAuth information to use with this StreamQuerier instance. (Check TwEater module help to create such a file.)")
	private String oAuthConfigurationName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.umd.rhsmith.diads.meater.modules.tweater.streaming.
	 * StreamQuerierInitializer#getoAuthConfigurationName()
	 */
	@Override
	public String getoAuthConfigurationName() {
		return oAuthConfigurationName;
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */
	@Override
	public String getUiDescription() {
		return TDESC;
	}

	@Override
	public void resetConfiguration() {
		this.oAuthConfigurationName = "";
	}

	@Override
	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.loadConfigurationPropertiesFrom(config);
		this.oAuthConfigurationName = config.getString(CKEY_OAUTH,
				this.oAuthConfigurationName);
	}

	@Override
	protected void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.saveConfigurationPropertiesTo(config);
		config.setProperty(CKEY_OAUTH, this.oAuthConfigurationName);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	public static final String TNAME = "TimelineCollector";
	public static final String TDESC = "A component which collects past statuses from users' timelines";

}
