package edu.umd.rhsmith.diads.meater.core.config.props;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.util.console.ConsolePrompter;
import edu.umd.rhsmith.diads.meater.util.console.BytePrompter;

public class ByteProperty extends BasicConfigProperty<Byte> {

	public ByteProperty(String name, Byte defaultVal, String uiName,
			String uiDescription) {
		super(name, defaultVal, uiName, uiDescription);
	}

	@Override
	public void loadVal(HierarchicalConfiguration config) {
		this.setVal(config.getByte(getName()));
	}

	@Override
	public void saveVal(HierarchicalConfiguration config) {
		config.setProperty(getName(), getVal());
	}

	@Override
	protected ConsolePrompter<? extends Byte> getConsolePrompter() {
		return BytePrompter.PROMPT;
	}

	@Override
	public String getPropertyTypeName() {
		return "byte";
	}
}
