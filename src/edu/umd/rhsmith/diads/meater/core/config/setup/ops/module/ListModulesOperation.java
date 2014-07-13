package edu.umd.rhsmith.diads.meater.core.config.setup.ops.module;

import edu.umd.rhsmith.diads.meater.core.config.ConfigModule;
import edu.umd.rhsmith.diads.meater.core.config.MEaterConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class ListModulesOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "list-modules";
	public static final String OP_UINAME = "List loaded modules";

	private final MEaterConfig owner;

	public ListModulesOperation(MEaterConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		if (this.owner.getNumModules() == 0) {
			setup.getConsole().say(MSG_NO_MODULES);
			return;
		}

		int idx = 0;

		for (ConfigModule m : this.owner.getModules()) {
			setup.getConsole().say(MSG_MODULE_NAME_FMT, idx, m.getModuleName(),
					m.getUiName());
			++idx;
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_MODULE_NAME_FMT = "%d) %s: %s";
	private static final String MSG_NO_MODULES = "(no loaded modules)";
}
