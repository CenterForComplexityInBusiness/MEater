package edu.umd.rhsmith.diads.meater.core.config.props;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;

public interface ConfigProperty<T> {

	public abstract String getName();

	public abstract String getUiName();

	public abstract String getUiDescription();

	public abstract T getVal();
	
	public abstract void setVal(T val);
	
	public abstract void reset();

	public abstract void loadVal(HierarchicalConfiguration config);

	public abstract void saveVal(HierarchicalConfiguration config);

	public abstract void setup(MEaterSetupConsole setup);
	
}
