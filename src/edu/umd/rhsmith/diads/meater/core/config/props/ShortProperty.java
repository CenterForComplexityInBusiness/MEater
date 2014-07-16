package edu.umd.rhsmith.diads.meater.core.config.props;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.util.console.ConsolePrompter;
import edu.umd.rhsmith.diads.meater.util.console.ShortPrompter;

public class ShortProperty extends BasicConfigProperty<Short> {

	public ShortProperty(String name, Short defaultVal, String uiName,
			String uiDescription) {
		super(name, defaultVal, uiName, uiDescription);
	}

	@Override
	public void loadVal(HierarchicalConfiguration config) {
		this.setVal(config.getShort(getName()));
	}

	@Override
	public void saveVal(HierarchicalConfiguration config) {
		config.setProperty(getName(), getVal());
	}

	@Override
	protected ConsolePrompter<? extends Short> getConsolePrompter() {
		return ShortPrompter.PROMPT;
	}
	@Override
	public String getPropertyTypeName() {
		return "short";
	}
}
