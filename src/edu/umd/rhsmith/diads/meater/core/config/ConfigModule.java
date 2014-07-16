package edu.umd.rhsmith.diads.meater.core.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.MEaterMain;
import edu.umd.rhsmith.diads.meater.core.app.ModuleInstantiationException;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentManager;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaRegistration;
import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfigContainer;

public abstract class ConfigModule extends
		InstanceConfigContainer<ComponentConfig> {

	// internal-use name of this module
	private final String moduleName;

	// media types made public by this module
	private final Set<MediaRegistration<?>> mediaRegs;
	private final Map<String, MediaRegistration<?>> namedMediaRegs;
	private final Map<Class<?>, MediaRegistration<?>> classMediaRegs;

	public ConfigModule(String moduleName) {
		super();
		this.moduleName = moduleName;

		this.mediaRegs = new HashSet<MediaRegistration<?>>();
		this.namedMediaRegs = new HashMap<String, MediaRegistration<?>>();
		this.classMediaRegs = new HashMap<Class<?>, MediaRegistration<?>>();
	}

	public void addTo(MEaterMain main) throws MEaterConfigurationException {
		this.instantiateComponents(main.getComponentManager());
	}

	public void instantiateComponents(ComponentManager componentManager)
			throws MEaterConfigurationException {
		for (ComponentConfig cc : getInstanceConfigs()) {
			cc.createComponentInstance(componentManager);
		}
	}

	/*
	 * --------------------------------
	 * Media registration
	 * --------------------------------
	 */

	public final <M> void registerMediaType(Class<M> mediaClass) {
		this.registerMediaType(new MediaRegistration<M>(mediaClass));
	}

	public final void registerMediaType(MediaRegistration<?> registration) {
		mediaRegs.add(registration);
		namedMediaRegs.put(registration.getMediaName(), registration);
		classMediaRegs.put(registration.getClass(), registration);
	}

	public boolean mediaTypeIsRegistered(Class<?> mediaClass) {
		return classMediaRegs.containsKey(mediaClass);
	}

	public boolean mediaTypeIsRegistered(String mediaName) {
		return namedMediaRegs.containsKey(mediaName);
	}

	@SuppressWarnings("unchecked")
	public <M> MediaRegistration<M> getMediaTypeRegistration(Class<M> mediaClass) {
		return (MediaRegistration<M>) classMediaRegs.get(mediaClass);
	}

	public MediaRegistration<?> getMediaTypeRegistration(String mediaName) {
		return namedMediaRegs.get(mediaName);
	}

	public Set<MediaRegistration<?>> getRegisteredMediaTypes() {
		return new HashSet<MediaRegistration<?>>(mediaRegs);
	}

	/*
	 * --------------------------------
	 * General getters/setters
	 * --------------------------------
	 */

	public String getModuleName() {
		return this.moduleName;
	}

	/*
	 * --------------------------------
	 * Load modules from class names
	 * --------------------------------
	 */

	public static ConfigModule forName(String name)
			throws ModuleInstantiationException {
		try {
			// try to get the class from the name
			@SuppressWarnings("unchecked")
			Class<? extends ConfigModule> moduleClass = (Class<? extends ConfigModule>) Class
					.forName(name);

			// try to get the constructor for the module - prefer nullary, use
			// named if necessary.
			Constructor<? extends ConfigModule> constructor;
			constructor = moduleClass.getConstructor();
			return constructor.newInstance();
		} catch (ClassNotFoundException | NoClassDefFoundError
				| ExceptionInInitializerError | SecurityException
				| InstantiationException | IllegalAccessException
				| InvocationTargetException | NoSuchMethodException e) {
			// wrap the various horrible things that can happen in this guy -
			// there's nothing we are interested in doing about any of them
			// other than saying "oh no".
			throw new ModuleInstantiationException(String.format(
					MSG_ERR_INSTANTIATION_FAILED_FMT, e.getMessage()), e);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_INSTANTIATION_FAILED_FMT = "Module instantiation failed - %s";
}
