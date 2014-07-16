package edu.umd.rhsmith.diads.meater.core.config.props;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.util.console.ConsolePrompter;
import edu.umd.rhsmith.diads.meater.util.console.FloatPrompter;

public class FloatProperty extends BasicConfigProperty<Float> {

	public FloatProperty(String name, Float defaultVal, String uiName,
			String uiDescription) {
		super(name, defaultVal, uiName, uiDescription);
	}

	@Override
	public void loadVal(HierarchicalConfiguration config) {
		this.setVal(config.getFloat(getName()));
	}

	@Override
	public void saveVal(HierarchicalConfiguration config) {
		config.setProperty(getName(), getVal());
	}

	@Override
	protected ConsolePrompter<? extends Float> getConsolePrompter() {
		return FloatPrompter.PROMPT;
	}
	@Override
	public String getPropertyTypeName() {
		return "float";
	}
}
