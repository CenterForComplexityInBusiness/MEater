package edu.umd.rhsmith.diads.meater.core.app;

import org.apache.commons.configuration.ConfigurationException;

public class MEaterConfigurationException extends MEaterException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MEaterConfigurationException(String message) {
		super(message);
	}

	public MEaterConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public MEaterConfigurationException(Throwable cause) {
		super(cause);
	}

	public MEaterConfigurationException(ConfigurationException cause) {
		super(String.format(MSG_ERR_BAD_FILE_FMT, cause.getMessage()), cause);
	}

	private static final String MSG_ERR_BAD_FILE_FMT = "Malformed configuration file: %s";
}
