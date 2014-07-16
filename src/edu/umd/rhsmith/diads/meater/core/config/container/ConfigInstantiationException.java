package edu.umd.rhsmith.diads.meater.core.config.container;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;

public class ConfigInstantiationException extends
		MEaterConfigurationException {

	public ConfigInstantiationException(String message) {
		super(message);
	}

	public ConfigInstantiationException(String message, Throwable t) {
		super(message, t);
	}

	public ConfigInstantiationException(Throwable t) {
		super(t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
