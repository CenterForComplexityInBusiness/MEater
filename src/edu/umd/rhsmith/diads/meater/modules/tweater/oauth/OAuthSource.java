package edu.umd.rhsmith.diads.meater.modules.tweater.oauth;


public interface OAuthSource {
	public OAuthInfo getOAuthInfo(String name) throws OAuthLoadException;
}
