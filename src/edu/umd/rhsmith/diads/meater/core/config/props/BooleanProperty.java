package edu.umd.rhsmith.diads.meater.core.config.props;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.util.console.ConsolePrompter;
import edu.umd.rhsmith.diads.meater.util.console.BooleanPrompter;

public class BooleanProperty extends BasicConfigProperty<Boolean> {

	public BooleanProperty(String name, Boolean defaultVal, String uiName,
			String uiDescription) {
		super(name, defaultVal, uiName, uiDescription);
	}

	@Override
	public void loadVal(HierarchicalConfiguration config) {
		this.setVal(config.getBoolean(getName()));
	}

	@Override
	public void saveVal(HierarchicalConfiguration config) {
		config.setProperty(getName(), getVal());
	}

	@Override
	protected ConsolePrompter<? extends Boolean> getConsolePrompter() {
		return BooleanPrompter.PROMPT_TRUEFALSE;
	}

	@Override
	public String getPropertyTypeName() {
		return "Boolean";
	}
}
