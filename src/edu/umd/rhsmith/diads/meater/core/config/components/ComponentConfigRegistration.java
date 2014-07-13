package edu.umd.rhsmith.diads.meater.core.config.components;

public interface ComponentConfigRegistration<C extends ComponentConfig> {
	public String getName();
	
	public String getDescription();
	
	public C createConfiguration() throws ComponentConfigInstantiationException;
}
