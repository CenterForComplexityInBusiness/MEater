package edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav;

import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;


public class ExitOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "exit";
	public static final String OP_UINAME = "Exit setup";

	public ExitOperation() {
		super(OP_UINAME, OP_SHORTNAME);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.exitSetupLoop();
	}

	/*
	 * --------------------------------
	 * Message strings
	 * --------------------------------
	 */
}
