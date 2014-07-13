package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.path;

import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class PathSourceRemoveOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "remove-path-source";
	private static final String OP_UINAME = "Remove a source from this media path";
	private MediaPathConfig owner;

	public PathSourceRemoveOperation(MediaPathConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().say(MSG_PROMPT_NAME);
		String name = setup.getConsole().prompt(false);
		if (this.owner.removeSource(name)) {
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

	private static final String MSG_ERR_FMT = "No source in path with name '%s'";
	private static final String MSG_PROMPT_NAME = "Enter the name of the media source to remove.";
	private static final String MSG_SUCCESS_FMT = "Media source '%s' removed.";

}
