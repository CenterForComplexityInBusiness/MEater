package edu.umd.rhsmith.diads.meater.core.config.setup.ops.component;

import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class RenameInstanceOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "rename-component";
	public static final String OP_UINAME = "Rename component";

	private final InstanceConfig owner;

	public RenameInstanceOperation(InstanceConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// get new name
		setup.getConsole().say(MSG_PROMPT);
		String newInstanceName = setup.getConsole().prompt(false);

		// set it
		this.owner.setInstanceName(newInstanceName);

		// yay we did it
		setup.getConsole().say(MSG_SUCCESS);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_PROMPT = "Enter new name for component:";
	private static final String MSG_SUCCESS = "Component renamed";

}
