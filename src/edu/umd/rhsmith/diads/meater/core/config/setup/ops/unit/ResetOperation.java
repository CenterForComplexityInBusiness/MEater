package edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit;

import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.util.console.BooleanPrompter;

public class ResetOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "reset";
	public static final String OP_UINAME = "Reset configuration unit";

	private final ConfigUnit owner;

	public ResetOperation(ConfigUnit owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().say(MSG_RESET_CONFIRM_FMT, this.owner.getUiName());
		if (setup.getConsole().prompt(BooleanPrompter.PROMPT_YESNO, false)) {
			this.owner.resetConfiguration();
		}
	}

	/*
	 * --------------------------------
	 * Message strings
	 * --------------------------------
	 */

	private static final String MSG_RESET_CONFIRM_FMT = "Really reset configuration unit '%s'?";
}
