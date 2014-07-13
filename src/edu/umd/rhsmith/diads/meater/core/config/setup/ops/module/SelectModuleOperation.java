package edu.umd.rhsmith.diads.meater.core.config.setup.ops.module;

import edu.umd.rhsmith.diads.meater.core.config.ConfigModule;
import edu.umd.rhsmith.diads.meater.core.config.MEaterConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class SelectModuleOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "select-module";
	public static final String OP_UINAME = "Select module for setup";

	private final MEaterConfig owner;

	public SelectModuleOperation(MEaterConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// handy hint: let user know if there are no loaded modules
		if (this.owner.getNumModules() == 0) {
			setup.getConsole().error(MSG_ERR_NO_MODULES_FMT,
					AddModuleOperation.OP_SHORTNAME);
			return;
		}

		// if we actually have modules to go to, ask user for choice
		setup.getConsole().say(MSG_PROMPT_SELECT_FMT,
				ListModulesOperation.OP_SHORTNAME);
		String moduleName = setup.getConsole().prompt(false);

		// try name lookup
		ConfigModule module = this.owner.getModuleByName(moduleName);
		if (module != null) {
			setup.selectUnit(module);
		} else {
			setup.getConsole().error(MSG_ERR_INVALID_NAME_FMT, moduleName,
					ListModulesOperation.OP_SHORTNAME);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_NO_MODULES_FMT = "No currently-loaded modules to select. (Use '%s' to load new modules)";
	private static final String MSG_ERR_INVALID_NAME_FMT = "Invalid module name specified: '%s' (use '%s' to list loaded modules)";
	private static final String MSG_PROMPT_SELECT_FMT = "Enter name of module to select";

}
