package edu.umd.rhsmith.diads.meater.core.config.setup.ops.component;

import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfigInstantiationException;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class AddComponentOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "add-component";
	public static final String OP_UINAME = "Add component instance to current unit";

	private final ComponentConfigContainer owner;

	public AddComponentOperation(ComponentConfigContainer owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// get name of submodule to add
		setup.getConsole().say(MSG_PROMPT_TYPE);
		String typeName = setup.getConsole().prompt(false);

		// verify that we can in fact create such a thing
		boolean supported = this.owner.supportsComponentType(typeName);
		if (!supported) {
			setup.getConsole().error(MSG_ERR_COULDNT_INSTANTIATE_FMT, typeName);
			return;
		}
		setup.getConsole().say(MSG_CONFIRM_FMT, typeName,
				this.owner.getComponentTypeDescription(typeName));

		// get name of new instance
		setup.getConsole().say(MSG_PROMPT_INSTANCE);
		String instanceName = setup.getConsole().prompt(false);

		// try to instantiate component
		ComponentConfig config;
		try {
			config = this.owner.addNewComponentConfiguration(typeName);
		} catch (ComponentConfigInstantiationException e) {
			setup.getConsole().error(MSG_ERR_COULDNT_INSTANTIATE_FMT, typeName,
					ListComponentTypesOperation.OP_SHORTNAME);
			return;
		}

		// set its instance name as the user desired
		config.setInstanceName(instanceName);

		// some components might have an operation to execute on creation
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

	private static final String MSG_PROMPT_TYPE = "Please enter the name of the component type you would like to add";
	private static final String MSG_CONFIRM_FMT = "Creating new %s";
	private static final String MSG_PROMPT_INSTANCE = "Name of new component instance?";
	private static final String MSG_ERR_COULDNT_INSTANTIATE_FMT = "Current unit does not support creating configuration type '%s' (use '%s' to list supported configuration types)";
	private static final String MSG_SUCCESS_FMT = "New %s successfully added";

}
