package edu.umd.rhsmith.diads.meater.core.config.components;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;

public class ComponentConfigInstantiationException extends
		MEaterConfigurationException {

	public ComponentConfigInstantiationException(String message) {
		super(message);
	}

	public ComponentConfigInstantiationException(String message, Throwable t) {
		super(message, t);
	}

	public ComponentConfigInstantiationException(Throwable t) {
		super(t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
