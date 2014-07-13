package edu.umd.rhsmith.diads.meater.core.config.setup.ops.module;

import edu.umd.rhsmith.diads.meater.core.config.ConfigModule;
import edu.umd.rhsmith.diads.meater.core.config.MEaterConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.util.console.BooleanPrompter;

public class RemoveModuleOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "remove-module";
	public static final String OP_UINAME = "Remove module";

	private final MEaterConfig owner;

	public RemoveModuleOperation(MEaterConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// get name of module to remove
		setup.getConsole().say(MSG_PROMPT);
		String moduleName = setup.getConsole().prompt(false);

		// make sure we have it
		ConfigModule module = this.owner.getModuleByName(moduleName);
		if (module == null) {
			setup.getConsole().error(MSG_ERR_INVALID_NAME_FMT, moduleName,
					ListModulesOperation.OP_SHORTNAME);
			return;
		}
		moduleName = module.getModuleName();

		// okay then
		setup.getConsole().warn(MSG_WARN_PROMPT_REMOVE_FMT, moduleName);
		if (setup.getConsole().prompt(BooleanPrompter.PROMPT_YESNO, false)) {
			this.owner.removeModule(module);
			setup.getConsole().say(MSG_SUCCESS);
		}
	}

	/*
	 * --------------------------------
	 * Message strings
	 * --------------------------------
	 */

	private static final String MSG_PROMPT = "Enter the name of the module you would like to remove";
	private static final String MSG_ERR_INVALID_NAME_FMT = "Invalid module name specified: '%s' (use '%s' to list loaded modules)";
	private static final String MSG_WARN_PROMPT_REMOVE_FMT = "Really remove module '%s' and all associated configuration information?";
	private static final String MSG_SUCCESS = "Module removed";

}
