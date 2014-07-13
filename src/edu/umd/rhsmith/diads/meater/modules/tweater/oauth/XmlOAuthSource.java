package edu.umd.rhsmith.diads.meater.modules.tweater.oauth;

import org.apache.commons.configuration.XMLConfiguration;

public class XmlOAuthSource implements OAuthSource {

	@Override
	public OAuthInfo getOAuthInfo(String name) throws OAuthLoadException {
		try {
			XMLConfiguration xml = new XMLConfiguration(name + ".xml");
			OAuthConfig oAuth = new OAuthConfig();
			oAuth.loadConfigurationFrom(xml);
			return oAuth;
		} catch (Exception e) {
			throw new OAuthLoadException(e);
		}
	}
}
