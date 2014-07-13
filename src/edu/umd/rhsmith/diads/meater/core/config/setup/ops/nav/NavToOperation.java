package edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav;

import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class NavToOperation extends SetupConsoleOperation {

	private final ConfigUnit to;

	public NavToOperation(String prettyName, String setupShortName,
			ConfigUnit to) {
		super(prettyName, setupShortName);

		this.to = to;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.selectUnit(this.to);
	}

}
