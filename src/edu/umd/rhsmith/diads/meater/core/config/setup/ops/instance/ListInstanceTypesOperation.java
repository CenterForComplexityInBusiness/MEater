package edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance;

import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfig;
import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class ListInstanceTypesOperation<I extends InstanceConfig> extends
		SetupConsoleOperation {

	public static final String OP_SHORTNAME = "list-instance-types";
	public static final String OP_UINAME = "List all instance types provided by current unit";

	private final InstanceConfigContainer<I> owner;

	public ListInstanceTypesOperation(InstanceConfigContainer<I> owner) {
		super(OP_UINAME, OP_SHORTNAME);
		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		int idx = 0;
		for (String typeName : this.owner.getInstanceConfigTypeNames()) {
			setup.getConsole().say(MSG_LIST_FMT, idx, typeName);
			String description = this.owner.getConfigTypeDescription(typeName);
			if (description != null && !description.isEmpty()) {
				setup.getConsole().pushIndent(1);
				setup.getConsole().say(description);
				setup.getConsole().popIndent();
			}
			++idx;
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
