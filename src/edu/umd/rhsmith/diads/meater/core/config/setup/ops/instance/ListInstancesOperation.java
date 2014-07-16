package edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance;

import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfig;
import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class ListInstancesOperation<I extends InstanceConfig> extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "list-instances";
	public static final String OP_UINAME = "List current unit's instances";

	private final InstanceConfigContainer<I> owner;

	public ListInstancesOperation(InstanceConfigContainer<I> owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		int idx = 0;
		for (InstanceConfig m : this.owner.getInstanceConfigs()) {
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
