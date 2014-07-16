package edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance;

import edu.umd.rhsmith.diads.meater.core.config.container.ConfigInstantiationException;
import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfig;
import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class AddInstanceOperation<I extends InstanceConfig> extends
		SetupConsoleOperation {

	public static final String OP_SHORTNAME = "add-instance";
	public static final String OP_UINAME = "Add instance to current unit";

	private final InstanceConfigContainer<I> owner;

	public AddInstanceOperation(InstanceConfigContainer<I> owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// get name of submodule to add
		setup.getConsole().say(MSG_PROMPT_TYPE);
		String typeName = setup.getConsole().prompt(false);

		// verify that we can in fact create such a thing
		boolean supported = this.owner.supportsConfigType(typeName);
		if (!supported) {
			setup.getConsole().error(MSG_ERR_COULDNT_INSTANTIATE_FMT, typeName);
			return;
		}
		setup.getConsole().say(MSG_CONFIRM_FMT, typeName,
				this.owner.getConfigTypeDescription(typeName));

		// get name of new instance
		setup.getConsole().say(MSG_PROMPT_INSTANCE);
		String instanceName = setup.getConsole().prompt(false);

		// try to instantiate instance
		I config;
		try {
			config = this.owner.addNewConfigUnit(typeName);
		} catch (ConfigInstantiationException e) {
			setup.getConsole().error(MSG_ERR_COULDNT_INSTANTIATE_FMT, typeName,
					ListInstanceTypesOperation.OP_SHORTNAME);
			return;
		}

		// set its instance name as the user desired
		config.setInstanceName(instanceName);

		// some instances might have an operation to execute on creation
		SetupConsoleOperation c = config.getCreationSetupConsoleOperation();
		if (c != null) {
			c.go(setup);
		}

		// yay we did it
		setup.getConsole().say(MSG_SUCCESS_FMT, config.getUiName(),
				config.getInstanceName());
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_PROMPT_TYPE = "Please enter the name of the instance type you would like to add";
	private static final String MSG_CONFIRM_FMT = "Creating new %s";
	private static final String MSG_PROMPT_INSTANCE = "Name of new instance?";
	private static final String MSG_ERR_COULDNT_INSTANTIATE_FMT = "Current unit does not support creating configuration type '%s' (use '%s' to list supported configuration types)";
	private static final String MSG_SUCCESS_FMT = "New %s successfully added";

}
