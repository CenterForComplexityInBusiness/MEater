package edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav;

import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;


public class HelpOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "help";
	public static final String OP_UINAME = "Show help";

	public HelpOperation() {
		super(OP_UINAME, OP_SHORTNAME);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.printHelp();
	}

	/*
	 * --------------------------------
	 * Message strings
	 * --------------------------------
	 */

}
