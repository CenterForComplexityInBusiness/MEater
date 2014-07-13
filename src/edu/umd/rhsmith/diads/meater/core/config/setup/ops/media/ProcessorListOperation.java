package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media;

import java.util.Map;

import edu.umd.rhsmith.diads.meater.core.app.components.media.Media;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class ProcessorListOperation extends SetupConsoleOperation {

	private static final String OP_SHORTNAME = "list-provided-processors";
	private static final String OP_UINAME = "List all media processors provided by this component";

	private ComponentConfig owner;

	public ProcessorListOperation(ComponentConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		int idx = 0;

		Map<String, Class<?>> ss = this.owner.getMediaProcessorTypes();

		if (ss.size() == 0) {
			setup.getConsole().say(MSG_NO_PROCS);
			return;
		}

		for (String sn : ss.keySet()) {
			String name = Media.handlerName(this.owner.getInstanceName(), sn);
			setup.getConsole().say(MSG_PROC_LIST_FMT, idx, name, ss.get(sn));
			++idx;
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_PROC_LIST_FMT = "%d) '%s' - handles instances of %s";
	private static final String MSG_NO_PROCS = "This component provides no media processors.";

}
