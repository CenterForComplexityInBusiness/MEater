package edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance;

import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfig;
import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.util.console.BooleanPrompter;

public class RemoveInstanceOperation<I extends InstanceConfig> extends
		SetupConsoleOperation {

	public static final String OP_SHORTNAME = "remove-instance";
	public static final String OP_UINAME = "Remove instance from current unit";

	private final InstanceConfigContainer<I> owner;

	public RemoveInstanceOperation(InstanceConfigContainer<I> owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// get name of submodule to remove
		setup.getConsole().say(MSG_PROMPT);
		String instanceName = setup.getConsole().prompt(false);

		// make sure we have it
		I config = this.owner.getInstanceConfigByName(instanceName);
		if (config == null) {
			setup.getConsole().error(MSG_ERR_INVALID_NAME_FMT, instanceName,
					ListInstancesOperation.OP_SHORTNAME);
			return;
		}

		// okay then
		setup.getConsole().say(MSG_WARN_PROMPT_REMOVE);
		if (setup.getConsole().prompt(BooleanPrompter.PROMPT_YESNO, false)) {
			this.owner.removeInstanceConfig(config);
			setup.getConsole().say(MSG_SUCCESS);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_PROMPT = "Enter the name of the instance you would like to remove";
	private static final String MSG_ERR_INVALID_NAME_FMT = "Invalid instance name specified: '%s' (use '%s' to list current unit's instances)";
	private static final String MSG_WARN_PROMPT_REMOVE = "Really remove instance '%s' and all associated configuration information?";
	private static final String MSG_SUCCESS = "instance removed";

}
