package edu.umd.rhsmith.diads.meater.core.config.setup.ops.component;

import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class ListComponentTypesOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "list-component-types";
	public static final String OP_UINAME = "List all component types provided by current unit";

	private final ComponentConfigContainer owner;

	public ListComponentTypesOperation(ComponentConfigContainer owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		int idx = 0;
		for (String typeName : this.owner.getComponentConfigurationTypeNames()) {
			setup.getConsole().say(MSG_LIST_FMT, idx, typeName);
			String description = this.owner.getComponentTypeDescription(typeName);
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
