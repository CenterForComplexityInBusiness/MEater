package edu.umd.rhsmith.diads.meater.core.app;


public class ModuleInstantiationException extends MEaterConfigurationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModuleInstantiationException(String message) {
		super(message);
	}

	public ModuleInstantiationException(String message, Throwable t) {
		super(message, t);
	}

}
