package edu.umd.rhsmith.diads.meater.core.config.setup.ops;

import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;

public abstract class SetupConsoleOperation {
	public static final String MSG_LIST_FMT = "%d) %s";

	private String uiName;
	private String shortName;

	public SetupConsoleOperation(String uiName, String shortName) {
		this.uiName = uiName;
		this.shortName = shortName;
	}

	public final void setUiName(String uiName) {
		this.uiName = uiName;
	}

	public final void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public final String getUiName() {
		return this.uiName;
	}

	public final String getShortName() {
		return this.shortName;
	}

	public abstract void go(MEaterSetupConsole setup);

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
