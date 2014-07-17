package edu.umd.rhsmith.diads.meater.core.app;

import edu.umd.rhsmith.diads.meater.util.ControlUnit;

public abstract class RuntimeModule extends ControlUnit {

	private MEaterMain main;
	private String name;

	public RuntimeModule(String moduleName) {
		this.name = moduleName;
		this.setLogName(moduleName);
	}

	void setMain(MEaterMain main) throws IllegalStateException {
		synchronized (this.controlLock) {
			this.requireUnStarted();
			this.main = main;
			this.setLogger(main.getLogger());
		}
	}

	public MEaterMain getMain() {
		return main;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
