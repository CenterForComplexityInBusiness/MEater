package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.path;

import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class PathProcessorListOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "list-path-processors";
	private static final String OP_UINAME = "List all current processors in this media path";

	private MediaPathConfig owner;

	public PathProcessorListOperation(MediaPathConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		int idx = 0;
		for (String m : this.owner.getProcessors()) {
			setup.getConsole().say(MSG_LIST_FMT, idx, m);
			++idx;
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
