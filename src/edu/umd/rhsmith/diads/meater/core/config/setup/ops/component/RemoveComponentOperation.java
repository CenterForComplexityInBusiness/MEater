package edu.umd.rhsmith.diads.meater.core.config.setup.ops.component;

import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.util.console.BooleanPrompter;

public class RemoveComponentOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "remove-component";
	public static final String OP_UINAME = "Remove component instance from current unit";

	private final ComponentConfigContainer owner;

	public RemoveComponentOperation(ComponentConfigContainer owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// get name of submodule to remove
		setup.getConsole().say(MSG_PROMPT);
		String instanceName = setup.getConsole().prompt(false);

		// make sure we have it
		ComponentConfig config = this.owner
				.getComponentConfigurationByName(instanceName);
		if (config == null) {
			setup.getConsole().error(MSG_ERR_INVALID_NAME_FMT, instanceName,
					ListComponentsOperation.OP_SHORTNAME);
			return;
		}

		// okay then
		setup.getConsole().say(MSG_WARN_PROMPT_REMOVE);
		if (setup.getConsole().prompt(BooleanPrompter.PROMPT_YESNO, false)) {
			this.owner.removeComponentConfiguration(config);
			setup.getConsole().say(MSG_SUCCESS);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_PROMPT = "Enter the name of the component instance you would like to remove";
	private static final String MSG_ERR_INVALID_NAME_FMT = "Invalid component name specified: '%s' (use '%s' to list current unit's component instances)";
	private static final String MSG_WARN_PROMPT_REMOVE = "Really remove component instance '%s' and all associated configuration information?";
	private static final String MSG_SUCCESS = "Component removed";

}
