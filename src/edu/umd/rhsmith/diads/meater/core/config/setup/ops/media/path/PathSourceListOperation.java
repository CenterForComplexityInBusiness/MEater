package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.path;

import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class PathSourceListOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "list-path-outputs";
	private static final String OP_UINAME = "List all current sources for this media path";

	private MediaPathConfig owner;

	public PathSourceListOperation(

	MediaPathConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		int idx = 0;
		for (String m : this.owner.getSources()) {
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
