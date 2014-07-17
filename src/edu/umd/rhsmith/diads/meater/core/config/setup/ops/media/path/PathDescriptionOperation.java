package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.path;

import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class PathDescriptionOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "set-path-description";
	private static final String OP_UINAME = "Add a description to this media path";
	private MediaPathConfig owner;

	public PathDescriptionOperation(MediaPathConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().say(MSG_PROMPT);
		String descr = setup.getConsole().prompt(true);
		this.owner.setDescription(descr);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
	
	private static final String MSG_PROMPT = "Enter the new description for this media path.";
}
