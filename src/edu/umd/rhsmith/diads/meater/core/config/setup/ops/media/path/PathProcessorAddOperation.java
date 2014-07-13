package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.path;

import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.util.console.IntPrompter;

public class PathProcessorAddOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "add-path-processor";
	private static final String OP_UINAME = "Add a processor to this media path";
	private MediaPathConfig owner;

	public PathProcessorAddOperation(MediaPathConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().say(MSG_PROMPT_NAME);
		String name = setup.getConsole().prompt(false);

		setup.getConsole().say(MSG_PROMPT_IDX);
		Integer index = setup.getConsole().prompt(IntPrompter.PROMPT, true);

		if (index == null) {
			this.owner.addProcessor(name);
		} else {
			this.owner.addProcessor(index, name);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
	
	private static final String MSG_PROMPT_IDX = "Enter the index in the processor chain at which to insert the processor. (Leave blank for end of chain)";
	private static final String MSG_PROMPT_NAME = "Enter the name of the media processor to insert.";
}
