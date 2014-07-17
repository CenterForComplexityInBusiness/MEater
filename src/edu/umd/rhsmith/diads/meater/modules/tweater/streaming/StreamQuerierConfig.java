package edu.umd.rhsmith.diads.meater.modules.tweater.streaming;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.props.StringProperty;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.UserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;

public class StreamQuerierConfig extends ComponentConfig implements
		StreamQuerierInitializer {

	public StreamQuerierConfig() {
		super();

		this.registerConfigProperty(oAuthConfigurationName);

		this.registerMediaSourceName(StreamQuerier.SRCNAME_TWEETS,
				UserStatusData.class);
		this.registerMediaProcessorName(StreamQuerier.PNAME_QADD,
				QueryItem.class);
		this.registerMediaProcessorName(StreamQuerier.PNAME_QRMV,
				QueryItem.class);
	}

	@Override
	public Component instantiateComponent() throws MEaterConfigurationException {
		return new StreamQuerier(this);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	private static final String CKEY_OAUTH = "oAuthName";
	private static final String DEFAULT_OAUTH = "oauth";
	private static final String UINAME_OAUTH = "OAuth Configuration file";
	private static final String UIDESC_OAUTH = "The name of the XML file containing the Twitter OAuth information to use with this instance. (Check TwEater module help to create such a file.)";
	private final StringProperty oAuthConfigurationName = new StringProperty(
			CKEY_OAUTH, DEFAULT_OAUTH, UINAME_OAUTH, UIDESC_OAUTH);

	@Override
	public String getoAuthConfigurationName() {
		return oAuthConfigurationName.getVal();
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

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	public static final String TNAME = "StreamQuerier";
	public static final String TDESC = "A component which submits queries to the Twitter streaming API to retrieve statuses";

}
