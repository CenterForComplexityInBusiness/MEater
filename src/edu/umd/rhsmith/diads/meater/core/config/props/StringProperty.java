package edu.umd.rhsmith.diads.meater.core.config.props;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.util.console.ConsolePrompter;
import edu.umd.rhsmith.diads.meater.util.console.StringPrompter;

public class StringProperty extends BasicConfigProperty<String> {

	public StringProperty(String name, String defaultVal, String uiName,
			String uiDescription) {
		super(name, defaultVal, uiName, uiDescription);
	}

	@Override
	public void loadVal(HierarchicalConfiguration config) {
		this.setVal(config.getString(getName()));
	}

	@Override
	public void saveVal(HierarchicalConfiguration config) {
		config.setProperty(getName(), getVal());
	}

	@Override
	protected ConsolePrompter<? extends String> getConsolePrompter() {
		return StringPrompter.PROMPT;
	}

	@Override
	public String getPropertyTypeName() {
		return "string";
	}
}
