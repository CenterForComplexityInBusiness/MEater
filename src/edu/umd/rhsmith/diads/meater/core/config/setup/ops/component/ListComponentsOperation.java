package edu.umd.rhsmith.diads.meater.core.config.setup.ops.component;

import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class ListComponentsOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "list-components";
	public static final String OP_UINAME = "List current unit's component instances";

	private final ComponentConfigContainer owner;

	public ListComponentsOperation(ComponentConfigContainer owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		int idx = 0;
		for (ComponentConfig m : this.owner.getComponentConfigurations()) {
			setup.getConsole().say(MSG_LIST_FMT, idx, m.getUiName());
			++idx;
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
