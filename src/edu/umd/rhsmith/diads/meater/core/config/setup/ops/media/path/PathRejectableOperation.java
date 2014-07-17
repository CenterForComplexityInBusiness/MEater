package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.path;

import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.util.console.BooleanPrompter;

public class PathRejectableOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "set-path-rejectable";
	private static final String OP_UINAME = "Set whether this media path is eligible to be skipped under heavy load";
	private MediaPathConfig owner;

	public PathRejectableOperation(MediaPathConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().say(MSG_PROMPT);
		setup.getConsole().divide(1);
		setup.getConsole().say(MSG_PROMPT_1);
		setup.getConsole().say(MSG_PROMPT_2);
		setup.getConsole().say(MSG_PROMPT_3);
		setup.getConsole().divide(1);
		setup.getConsole().say(MSG_PROMPT_FINAL_FMT,
				String.valueOf(this.owner.isRejectable()));

		boolean rejectable = setup.getConsole().prompt(
				BooleanPrompter.PROMPT_YESNO, false);
		this.owner.setRejectable(rejectable);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_PROMPT = "This operation will toggle whether this media path may be skipped if the machine running MEater is under heavy load.";
	private static final String MSG_PROMPT_1 = "This option is recommended for paths sourced from active collections, such as Twitter API collections.";
	private static final String MSG_PROMPT_2 = "This option is ***NOT*** recommended for paths which are used to toggle behavior of media components, such as removing expired queries from a querier.";
	private static final String MSG_PROMPT_3 = "(See the 'resource limit rejection threshold' property in MEater's general settings)";
	private static final String MSG_PROMPT_FINAL_FMT = "Enable load rejection for this path? (currently set to: %s)";
}
