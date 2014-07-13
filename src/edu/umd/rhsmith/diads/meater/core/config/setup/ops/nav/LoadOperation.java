package edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav;

import org.apache.commons.configuration.ConfigurationException;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class LoadOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "load";
	public static final String OP_UINAME = "Load configuration from file";

	public LoadOperation() {
		super(OP_UINAME, OP_SHORTNAME);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().say(MSG_PROMPT_FILENAME);
		String filename = setup.getConsole().prompt(false);
		try {
			setup.loadFile(filename);
		} catch (ConfigurationException | MEaterConfigurationException e) {
			setup.getConsole().error(MSG_ERR_CONFIG_FMT, e.getMessage());
		}
	}

	/*
	 * --------------------------------
	 * Message strings
	 * --------------------------------
	 */
	private static final String MSG_PROMPT_FILENAME = "Please enter the filename to load from";
	private static final String MSG_ERR_CONFIG_FMT = "Unable to load configuration file: %s";

}
