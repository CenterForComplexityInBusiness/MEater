package edu.umd.rhsmith.diads.meater.modules.tweater.oauth;

import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.EditExternalUnitOperation;

public class EditOAuthOperation extends
		EditExternalUnitOperation<OAuthConfig> {

	public EditOAuthOperation() {
		super("Set up or edit an OAuth information set", "setup-oauth");
	}

	@Override
	protected OAuthConfig createUnit() {
		OAuthConfig o = new OAuthConfig();
		o.resetInternalConfiguration();
		return o;
	}

}
