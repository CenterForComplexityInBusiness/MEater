package edu.umd.rhsmith.diads.meater.modules.tweater;

import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class SetupLegacyDbOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "setup-legacy-db";
	public static final String OP_UINAME = "Set up a legacy TwEater storage database";

	public SetupLegacyDbOperation() {
		super(OP_UINAME, OP_SHORTNAME);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		/*
		 * I don't really know what to do here. So, for now, just tell people to
		 * use the old script. Maybe one day I will port that script into this
		 * class.
		 */

		setup.getConsole().say(MSG_WHAT);
		setup.getConsole().divide(1);
		setup.getConsole().say(MSG_WHERE);
		setup.getConsole().divide(1);
	}

	/*
	 * --------------------------------
	 * Messages strings
	 * --------------------------------
	 */
	
	private static final String MSG_WHERE = "If you did not recieve such a file with your TwEater module, it may be found in the distribution of the original TwEater application at either http://www.github.com/dmonner/tweater or http://www.github.com/rmachedo/tweater.";
	private static final String MSG_WHAT = "To set up a legacy TwEater database, find the Python script entitled \"setup-mysql\". Follow the script's instructions and then run the resulting sql command file in your MySQL installation.";
}
