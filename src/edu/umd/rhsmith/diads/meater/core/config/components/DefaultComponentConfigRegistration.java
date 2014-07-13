package edu.umd.rhsmith.diads.meater.core.config.components;

public class DefaultComponentConfigRegistration<C extends ComponentConfig>
		implements ComponentConfigRegistration<C> {

	private String name;
	private String desc;
	private Class<C> typeClass;

	public DefaultComponentConfigRegistration(Class<C> typeClass) {
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
	public C createConfiguration() throws ComponentConfigInstantiationException {
		try {
			return this.typeClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ComponentConfigInstantiationException(e);
		}
	}

}
