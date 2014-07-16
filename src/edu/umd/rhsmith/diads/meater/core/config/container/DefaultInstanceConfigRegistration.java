package edu.umd.rhsmith.diads.meater.core.config.container;

import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;


public class DefaultInstanceConfigRegistration<U extends ConfigUnit>
		implements InstanceConfigRegistration<U> {

	private String name;
	private String desc;
	private Class<U> typeClass;

	public DefaultInstanceConfigRegistration(Class<U> typeClass) {
		this.typeClass = typeClass;
		this.name = extractName(typeClass);
		this.desc = extractDesc(typeClass);
	}

	public static String extractName(Class<?> typeClass) {
		try {
			return (String) typeClass.getField("TNAME").get(null);
		} catch (Exception e) {
			return typeClass.getSimpleName();
		}
	}

	public static String extractDesc(Class<?> typeClass) {
		try {
			return (String) typeClass.getField("TDESC").get(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.desc;
	}

	@Override
	public U createConfig() throws ConfigInstantiationException {
		try {
			return this.typeClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ConfigInstantiationException(e);
		}
	}

}
