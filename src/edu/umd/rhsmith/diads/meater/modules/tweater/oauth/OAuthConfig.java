package edu.umd.rhsmith.diads.meater.modules.tweater.oauth;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;

public class OAuthConfig extends ConfigUnit implements OAuthInfo {

	private static final String CKEY_ACCESS_TOKEN_SECRET = "accessTokenSecret";
	private static final String CKEY_ACCESS_TOKEN = "accessToken";
	private static final String CKEY_CONSUMER_SECRET = "consumerSecret";
	private static final String CKEY_CONSUMER_KEY = "consumerKey";

	public static final String TWEATER_CONSUMER_KEY = "RTojEz16nwhI3IrBrZpNQ";
	public static final String TWEATER_CONSUMER_SECRET = "lNfVdu2cFKrlEbaw1OiM2Y3TgVKLGBI3AuEEblZilek";

	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;

	public OAuthConfig() {
		super();

		this.registerSetupConsoleOperation(new OAuthSetupOperation(this));
	}

	@Override
	public String getConsumerKey() {
		return consumerKey;
	}

	@Override
	public String getConsumerSecret() {
		return consumerSecret;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setConsumerInfo(String consumerKey, String consumerSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public void setAccessInfo(String accessToken, String accessTokenSecret) {
		this.accessToken = consumerKey;
		this.accessTokenSecret = consumerSecret;
	}

	@Override
	public String getUiName() {
		return "OAuth Configuration";
	}

	@Override
	public String getUiDescription() {
		return "Authorization information required to connect to the Twitter API";
	}

	@Override
	public void resetConfiguration() {
		this.consumerKey = TWEATER_CONSUMER_KEY;
		this.consumerSecret = TWEATER_CONSUMER_SECRET;
		this.accessToken = "";
		this.accessTokenSecret = "";
	}

	@Override
	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		this.consumerKey = config
				.getString(CKEY_CONSUMER_KEY, this.consumerKey);
		this.consumerSecret = config.getString(CKEY_CONSUMER_SECRET,
				this.consumerSecret);
		this.accessToken = config
				.getString(CKEY_ACCESS_TOKEN, this.accessToken);
		this.accessTokenSecret = config.getString(CKEY_ACCESS_TOKEN_SECRET,
				this.accessTokenSecret);
	}

	@Override
	protected void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		config.setProperty(CKEY_CONSUMER_KEY, this.consumerKey);
		config.setProperty(CKEY_CONSUMER_SECRET, this.consumerSecret);
		config.setProperty(CKEY_ACCESS_TOKEN, this.accessToken);
		config.setProperty(CKEY_ACCESS_TOKEN_SECRET, this.accessTokenSecret);
	}

}
