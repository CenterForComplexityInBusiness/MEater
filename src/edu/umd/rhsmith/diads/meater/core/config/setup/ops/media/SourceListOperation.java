package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media;

import java.util.Map;

import edu.umd.rhsmith.diads.meater.core.app.components.media.Media;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class SourceListOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "list-provided-sources";
	private static final String OP_UINAME = "List all media sources provided by this media path";

	private ComponentConfig owner;

	public SourceListOperation(ComponentConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		int idx = 0;

		Map<String, Class<?>> ps = this.owner.getMediaSourceTypes();

		if (ps.size() == 0) {
			setup.getConsole().say(MSG_NO_SRCS);
			return;
		}

		for (String pn : ps.keySet()) {
			String name = Media.handlerName(this.owner.getInstanceName(), pn);
			setup.getConsole().say(MSG_SRC_LIST_FMT, idx, name, ps.get(pn));
			++idx;
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_SRC_LIST_FMT = "%d) '%s' - produces instances of %s";
	private static final String MSG_NO_SRCS = "This component provides no media sources.";

}
