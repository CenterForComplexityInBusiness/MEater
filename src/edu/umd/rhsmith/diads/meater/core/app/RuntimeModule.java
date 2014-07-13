package edu.umd.rhsmith.diads.meater.core.app;

import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.meater.util.ControlUnit;

public abstract class RuntimeModule extends ControlUnit {

	private final MEaterMain main;
	private String name;

	public RuntimeModule(String moduleName, MEaterMain main)
			throws IllegalStateException, ModuleAlreadyLoadedException,
			MEaterConfigurationException {
		this.name = moduleName;
		this.main = main;
		this.setLogName(moduleName);
		this.setLogger(main.getLogger());

		this.main.addRuntimeModule(this);
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

	@Override
	protected void doStartupRoutine() throws ControlException {

	}

	@Override
	protected void doShutdownRoutine() {

	}
}
