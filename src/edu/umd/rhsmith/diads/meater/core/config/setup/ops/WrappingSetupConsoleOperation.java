package edu.umd.rhsmith.diads.meater.core.config.setup.ops;

import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;

public class WrappingSetupConsoleOperation extends SetupConsoleOperation {

	private final SetupConsoleOperation delegate;

	public WrappingSetupConsoleOperation(String uiName, String shortName,
			SetupConsoleOperation delegate) {
		super(uiName, shortName);

		this.delegate = delegate;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		this.delegate.go(setup);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
