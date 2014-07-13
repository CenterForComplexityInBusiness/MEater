package edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav;

import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class InfoOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "info";
	public static final String OP_UINAME = "Show current component info";

	public InfoOperation() {
		super(OP_UINAME, OP_SHORTNAME);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		if (setup.getSelectedUnit() == null) {
			setup.getConsole().say(MSG_ERR_NO_SELECTED);
			return;
		}

		setup.getConsole().say("%s", setup.getSelectedUnit().getUiName());
		setup.getConsole()
				.say("%s", setup.getSelectedUnit().getUiDescription());
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_NO_SELECTED = "(No configuration unit selected)";
}
