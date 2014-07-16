package edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit;

import java.util.List;

import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.props.ConfigProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class SetupPropertiesOperation extends SetupConsoleOperation {

	public static final String OP_SHORTNAME = "setup-props";
	public static final String OP_UINAME = "Set up current selection's properties";

	private final ConfigUnit owner;

	public SetupPropertiesOperation(String uiName, String shortName,
			ConfigUnit owner) {
		super(uiName, shortName);
		this.owner = owner;
	}

	public SetupPropertiesOperation(ConfigUnit owner) {
		this(OP_UINAME, OP_SHORTNAME, owner);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		List<ConfigProperty<?>> props = this.owner.getConfigProperties();
		if (props.isEmpty()) {
			setup.getConsole().say(MSG_NO_FIELDS_FMT, this.owner.getUiName());
			return;
		}

		for (ConfigProperty<?> prop : props) {
			prop.setup(setup);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
	private static final String MSG_NO_FIELDS_FMT = "Component %s had no fields to set up";
}
