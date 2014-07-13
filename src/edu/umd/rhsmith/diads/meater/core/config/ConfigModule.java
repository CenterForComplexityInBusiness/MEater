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
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaRegistration;

public abstract class ConfigModule extends ComponentConfigContainer {

	// internal-use name of this module
	private final String moduleName;

	// media types made public by this module
	private final Set<MediaRegistration<?>> registrations;
	private final Map<String, MediaRegistration<?>> namedRegistrations;
	private final Map<Class<?>, MediaRegistration<?>> classRegistrations;

	public ConfigModule(String moduleName) {
		super();
		this.moduleName = moduleName;

		this.registrations = new HashSet<MediaRegistration<?>>();
		this.namedRegistrations = new HashMap<String, MediaRegistration<?>>();
		this.classRegistrations = new HashMap<Class<?>, MediaRegistration<?>>();
	}

	public void addTo(MEaterMain main) throws MEaterConfigurationException {
		this.instantiateComponents(main.getComponentManager());
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
		registrations.add(registration);
		namedRegistrations.put(registration.getMediaName(), registration);
		classRegistrations.put(registration.getClass(), registration);
	}

	public boolean mediaTypeIsRegistered(Class<?> mediaClass) {
		return classRegistrations.containsKey(mediaClass);
	}

	public boolean mediaTypeIsRegistered(String mediaName) {
		return namedRegistrations.containsKey(mediaName);
	}

	@SuppressWarnings("unchecked")
	public <M> MediaRegistration<M> getMediaTypeRegistration(Class<M> mediaClass) {
		return (MediaRegistration<M>) classRegistrations.get(mediaClass);
	}

	public MediaRegistration<?> getMediaTypeRegistration(String mediaName) {
		return namedRegistrations.get(mediaName);
	}

	public Set<MediaRegistration<?>> getRegisteredMediaTypes() {
		return new HashSet<MediaRegistration<?>>(registrations);
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
