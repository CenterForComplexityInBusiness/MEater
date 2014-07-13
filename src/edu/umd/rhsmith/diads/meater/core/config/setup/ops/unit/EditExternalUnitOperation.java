package edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit;

import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public abstract class EditExternalUnitOperation<U extends ConfigUnit>
		extends SetupConsoleOperation {

	public EditExternalUnitOperation(String uiName, String shortName) {
		super(uiName, shortName);
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		// create a unit
		U unit = this.createUnit();

		// get a filename
		setup.getConsole().say("Enter filename of unit to edit");
		String filename = setup.getConsole().prompt(false);

		// select the unit + filename
		setup.selectExternalUnit(unit, filename);
	}

	protected abstract U createUnit();
}
