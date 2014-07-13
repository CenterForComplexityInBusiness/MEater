package edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav;

import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class NavCloseOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "close";
	public static final String OP_UINAME = "Close current configuration unit";

	public NavCloseOperation() {
		super(OP_UINAME, OP_SHORTNAME);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.closeSelectedUnit();
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

}
