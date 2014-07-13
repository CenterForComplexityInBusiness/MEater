package edu.umd.rhsmith.diads.meater.core.config.setup.ops.module;

import edu.umd.rhsmith.diads.meater.core.app.ModuleAlreadyLoadedException;
import edu.umd.rhsmith.diads.meater.core.app.ModuleInstantiationException;
import edu.umd.rhsmith.diads.meater.core.config.ConfigModule;
import edu.umd.rhsmith.diads.meater.core.config.MEaterConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class AddModuleOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "add-module";
	public static final String OP_UINAME = "Load and add new module to configuration";

	private final MEaterConfig owner;

	public AddModuleOperation(MEaterConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// get name of module to add
		setup.getConsole().say(MSG_PROMPT);
		setup.getConsole().say(MSG_EXAMPLE_FMT,
				ConfigModule.class.getName());
		String className = setup.getConsole().prompt(false);

		// try to instantiate module
		ConfigModule module;
		try {
			// wrap around forName - a convenience method
			module = this.owner.addModule(className);
		} catch (ModuleInstantiationException e) {
			setup.getConsole().say(MSG_ERR_COULDNT_INSTANTIATE_FMT,
					e.getMessage());
			return;
		} catch (ModuleAlreadyLoadedException e) {
			setup.getConsole().say(MSG_ERR_COULDNT_ADD_FMT, e.getMessage());
			return;
		}

		// yay we did it
		setup.getConsole().say(MSG_SUCCESS_FMT, module.getUiName());
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_PROMPT = "Enter the fully-qualified class name of the module you would like to add.";
	private static final String MSG_EXAMPLE_FMT = "(Example: %s)";
	private static final String MSG_ERR_COULDNT_INSTANTIATE_FMT = "Unable to instantiate module class: %s";
	private static final String MSG_ERR_COULDNT_ADD_FMT = "Unable to add module: %s";
	private static final String MSG_SUCCESS_FMT = "Module '%s' successfully added";

}
