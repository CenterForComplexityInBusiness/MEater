package edu.umd.rhsmith.diads.meater.modules.tweater;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.RuntimeModule;
import edu.umd.rhsmith.diads.meater.modules.tweater.oauth.OAuthInfo;
import edu.umd.rhsmith.diads.meater.modules.tweater.oauth.OAuthLoadException;
import edu.umd.rhsmith.diads.meater.modules.tweater.oauth.OAuthSource;
import edu.umd.rhsmith.diads.meater.modules.tweater.oauth.XmlOAuthSource;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.meater.util.Util;

public class TwitterManager extends RuntimeModule {

	// TODO manage & supply twitter stream / other query objects?
	
	private OAuthSource oAuthSource;

	public TwitterManager() throws MEaterConfigurationException {
		super("TwitterManager");
		this.oAuthSource = new XmlOAuthSource();
	}

	public void setOAuthSource(OAuthSource source) {
		this.oAuthSource = source;
	}

	public OAuthSource getOAuthSource() {
		return this.oAuthSource;
	}

	public OAuthInfo getOAuthInfo(String name) throws NullPointerException {
		if(name == null) {
			throw new NullPointerException();
		}
		
		if (this.oAuthSource == null) {
			this.logSevere(MSG_ERR_NO_SRC);
			return null;
		}
		try {
			return this.oAuthSource.getOAuthInfo(name);
		} catch (OAuthLoadException e) {
			this.logSevere(MSG_ERR_SRC_FAILED_FMT, name, Util.traceMessage(e));
			return null;
		}
	}

	@Override
	protected void doStartupRoutine() throws ControlException {
	}

	@Override
	protected void doShutdownRoutine() {
	}

	private static final String MSG_ERR_SRC_FAILED_FMT = "OAuth source unable to retrieve authorization %s: %s";
	private static final String MSG_ERR_NO_SRC = "OAuth information requested with no available source!";
}
