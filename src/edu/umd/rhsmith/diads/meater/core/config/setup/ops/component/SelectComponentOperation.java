package edu.umd.rhsmith.diads.meater.core.config.setup.ops.component;

import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class SelectComponentOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "select-component";
	public static final String OP_UINAME = "Select component instance from current unit for setup";

	private final ComponentConfigContainer owner;

	public SelectComponentOperation(ComponentConfigContainer owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// handy hint: let user know if there are no loaded modules
		if (this.owner.getNumComponentConfigurations() == 0) {
			setup.getConsole().error(MSG_ERR_NO_COMPONENTS_FMT,
					AddComponentOperation.OP_SHORTNAME);
			return;
		}

		// if we actually have modules to go to, ask user for choice
		setup.getConsole().say(MSG_PROMPT_SELECT_FMT,
				ListComponentsOperation.OP_SHORTNAME);
		String instanceName = setup.getConsole().prompt(false);

		// try name lookup
		ComponentConfig config = this.owner
				.getComponentConfigurationByName(instanceName);
		if (config != null) {
			setup.selectUnit(config);
		} else {
			setup.getConsole().error(MSG_ERR_INVALID_NAME_FMT, instanceName,
					ListComponentsOperation.OP_SHORTNAME);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_NO_COMPONENTS_FMT = "No component instances in current unit to select. (Use '%s' to add new component instances)";
	private static final String MSG_ERR_INVALID_NAME_FMT = "No component instance in current unit with specified name '%s'. (Use '%s' to list component instances)";
	private static final String MSG_PROMPT_SELECT_FMT = "Enter name of component instance to select";

}
