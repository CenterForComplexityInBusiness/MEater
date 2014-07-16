package edu.umd.rhsmith.diads.meater.core.config.props;

import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.util.console.ConsolePrompter;

public abstract class BasicConfigProperty<T> implements ConfigProperty<T> {

	private final String name;
	private final String uiName;
	private final String uiDescription;

	private T val;
	private final T defaultVal;

	public BasicConfigProperty(String name, T defaultVal, String uiName,
			String uiDescription) {
		this.name = name;
		this.defaultVal = defaultVal;
		this.val = defaultVal;

		this.uiName = uiName;
		this.uiDescription = uiDescription;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUiName() {
		return uiName;
	}

	@Override
	public String getUiDescription() {
		return uiDescription;
	}

	public T getDefaultVal() {
		return defaultVal;
	}

	@Override
	public void reset() {
		this.val = this.defaultVal;
	}

	public T getVal() {
		return this.val;
	}

	public void setVal(T val) {
		if (val == null) {
			this.val = this.defaultVal;
		}

		this.val = val;
	}

	@Override
	public void setup(MEaterSetupConsole setup) {
		setup.getConsole()
				.say(MSG_INFO_FMT, getUiName(), getPropertyTypeName());
		if (getUiDescription() != null && !getUiDescription().isEmpty()) {
			setup.getConsole().say(MSG_HELPTEXT_FMT, getUiDescription());
		}
		setup.getConsole().say(MSG_CURRVAL_FMT, getVal());

		this.setVal(setup.getConsole().prompt(getConsolePrompter(), true));
	}

	public abstract String getPropertyTypeName();

	protected abstract ConsolePrompter<? extends T> getConsolePrompter();

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_CURRVAL_FMT = "(current value: '%s' -- enter empty line to retain)";
	private static final String MSG_HELPTEXT_FMT = "-- %s";
	private static final String MSG_INFO_FMT = "Property: %s (%s)";
}
