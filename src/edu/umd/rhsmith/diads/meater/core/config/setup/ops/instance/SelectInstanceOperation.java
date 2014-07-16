package edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance;

import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfig;
import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class SelectInstanceOperation<I extends InstanceConfig> extends
		SetupConsoleOperation {

	public static final String OP_SHORTNAME = "select-instance";
	public static final String OP_UINAME = "Select instance from current unit for setup";

	private final InstanceConfigContainer<I> owner;

	public SelectInstanceOperation(InstanceConfigContainer<I> owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// handy hint: let user know if there are no loaded modules
		if (this.owner.getNumInstanceConfigs() == 0) {
			setup.getConsole().error(MSG_ERR_NO_instanceS_FMT,
					AddInstanceOperation.OP_SHORTNAME);
			return;
		}

		// if we actually have modules to go to, ask user for choice
		setup.getConsole().say(MSG_PROMPT_SELECT_FMT,
				ListInstancesOperation.OP_SHORTNAME);
		String instanceName = setup.getConsole().prompt(false);

		// try name lookup
		I config = this.owner.getInstanceConfigByName(instanceName);
		if (config != null) {
			setup.selectUnit(config);
		} else {
			setup.getConsole().error(MSG_ERR_INVALID_NAME_FMT, instanceName,
					ListInstancesOperation.OP_SHORTNAME);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_NO_instanceS_FMT = "No instances in current unit to select. (Use '%s' to add new instances)";
	private static final String MSG_ERR_INVALID_NAME_FMT = "No instance in current unit with specified name '%s'. (Use '%s' to list instances)";
	private static final String MSG_PROMPT_SELECT_FMT = "Enter name of instance to select";

}
