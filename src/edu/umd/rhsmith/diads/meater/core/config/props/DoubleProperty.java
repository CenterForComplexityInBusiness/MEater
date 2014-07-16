package edu.umd.rhsmith.diads.meater.core.config.props;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.util.console.ConsolePrompter;
import edu.umd.rhsmith.diads.meater.util.console.DoublePrompter;

public class DoubleProperty extends BasicConfigProperty<Double> {

	public DoubleProperty(String name, Double defaultVal, String uiName,
			String uiDescription) {
		super(name, defaultVal, uiName, uiDescription);
	}

	@Override
	public void loadVal(HierarchicalConfiguration config) {
		this.setVal(config.getDouble(getName()));
	}

	@Override
	public void saveVal(HierarchicalConfiguration config) {
		config.setProperty(getName(), getVal());
	}

	@Override
	protected ConsolePrompter<? extends Double> getConsolePrompter() {
		return DoublePrompter.PROMPT;
	}
	
	@Override
	public String getPropertyTypeName() {
		return "double";
	}
}
