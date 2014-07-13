package edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav;

import org.apache.commons.configuration.ConfigurationException;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class SaveOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "save";
	public static final String OP_UINAME = "Save current configuration";

	public SaveOperation() {
		super(OP_UINAME, OP_SHORTNAME);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		try {
			setup.saveFile();
		} catch (ConfigurationException | MEaterConfigurationException e) {
			setup.getConsole().error(MSG_ERR_CONFIG_FMT, e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
	private static final String MSG_ERR_CONFIG_FMT = "Unable to save configuration file: %s";

}
