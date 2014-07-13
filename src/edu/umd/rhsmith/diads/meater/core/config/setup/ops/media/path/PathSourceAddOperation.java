package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.path;

import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class PathSourceAddOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "add-path-output";
	private static final String OP_UINAME = "Add an output processor to this media path";

	private MediaPathConfig owner;

	public PathSourceAddOperation(

	MediaPathConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().say(MSG_PROMPT_NAME);
		String name = setup.getConsole().prompt(false);
		this.owner.addSource(name);
		setup.getConsole().say(MSG_SUCCESS, name);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_SUCCESS = "Media source '%s' added";
	private static final String MSG_PROMPT_NAME = "Enter the name of the media source to add.";

}
