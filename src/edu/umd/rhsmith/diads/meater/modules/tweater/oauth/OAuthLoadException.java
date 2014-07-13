package edu.umd.rhsmith.diads.meater.modules.tweater.oauth;

import edu.umd.rhsmith.diads.meater.core.app.MEaterException;

public class OAuthLoadException extends MEaterException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OAuthLoadException(String message) {
		super(message);
	}

	public OAuthLoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public OAuthLoadException(Throwable cause) {
		super(cause);
	}
}
