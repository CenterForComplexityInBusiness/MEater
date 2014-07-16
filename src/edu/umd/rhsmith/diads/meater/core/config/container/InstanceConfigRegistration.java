package edu.umd.rhsmith.diads.meater.core.config.container;

import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;


public interface InstanceConfigRegistration<U extends ConfigUnit> {
	public String getName();
	
	public String getDescription();
	
	public U createConfig() throws ConfigInstantiationException;
}
