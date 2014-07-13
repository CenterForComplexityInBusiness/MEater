package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.path;

import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class PathProcessorRemoveOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "remove-path-processor";
	private static final String OP_UINAME = "Remove a processor from this media path";

	private MediaPathConfig owner;

	public PathProcessorRemoveOperation(

	MediaPathConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);
		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().say(MSG_PROMPT_NAME);
		String name = setup.getConsole().prompt(false);
		if (this.owner.removeProcessor(name)) {
			setup.getConsole().say(MSG_SUCCESS_FMT, name);
		} else {
			setup.getConsole().say(MSG_ERR_FMT, name);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_FMT = "No processor in path with name '%s'";
	private static final String MSG_SUCCESS_FMT = "Preprocessor '%s' removed.";
	private static final String MSG_PROMPT_NAME = "Enter the name of the processor to remove.";

}
