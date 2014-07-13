package edu.umd.rhsmith.diads.meater.core.app;

public class ModuleAlreadyLoadedException extends MEaterConfigurationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModuleAlreadyLoadedException(String moduleName) {
		super(String.format(MSG_ERR_MODULE_ALREADY_LOADED_FMT, moduleName));
	}

	private static final String MSG_ERR_MODULE_ALREADY_LOADED_FMT = "an instance of the module %s has already been loaded";
}
