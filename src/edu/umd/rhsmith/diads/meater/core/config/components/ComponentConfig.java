package edu.umd.rhsmith.diads.meater.core.config.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentInitializer;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentManager;
import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfig;

public abstract class ComponentConfig extends InstanceConfig implements
		ComponentInitializer {

	private final Map<String, Class<?>> mediaSourceNames;
	private final Map<String, Class<?>> mediaProcessorNames;

	public ComponentConfig() {
		super();

		this.mediaSourceNames = new HashMap<String, Class<?>>();
		this.mediaProcessorNames = new HashMap<String, Class<?>>();
	}

	public final Component createComponentInstance(ComponentManager mgr)
			throws MEaterConfigurationException {
		Component c = this.instantiateComponent();
		for (String s : this.mediaSourceNames.keySet()) {
			c.expectMediaProcessor(s, this.mediaSourceNames.get(s));
		}
		for (String s : this.mediaProcessorNames.keySet()) {
			c.expectMediaProcessor(s, this.mediaProcessorNames.get(s));
		}
		return c;
	}

	protected abstract Component instantiateComponent()
			throws MEaterConfigurationException;

	/*
	 * --------------------------------
	 * General getters/setters
	 * --------------------------------
	 */

	/*
	 * --------------------------------
	 * Media interaction
	 * --------------------------------
	 */

	public final Map<String, Class<?>> getMediaSourceTypes() {
		return new HashMap<String, Class<?>>(this.mediaSourceNames);
	}

	protected final void registerMediaSourceName(String name,
			Class<?> mediaClass) {
		this.mediaSourceNames.put(name, mediaClass);
	}

	protected void unregisterMediaSourceName(String name) {
		this.mediaSourceNames.remove(name);
	}

	public final Map<String, Class<?>> getMediaProcessorTypes() {
		return new HashMap<String, Class<?>>(this.mediaProcessorNames);
	}

	protected final void registerMediaProcessorName(String name,
			Class<?> mediaClass) {
		this.mediaProcessorNames.put(name, mediaClass);
	}

	protected final void unregisterMediaProcessorName(String name) {
		this.mediaProcessorNames.remove(name);
	}

	/*
	 * --------------------------------
	 * UI
	 * --------------------------------
	 */

	@Override
	public String getUiName() {
		return String.format("%s - %s", this.getRegisteredTypeName(), this
				.getInstanceName());
	}

	@Override
	public String getUiDescription() {
		return super.getUiDescription();
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	@Override
	protected void loadInternalConfigurationFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.loadInternalConfigurationFrom(config);
	}

	@Override
	public void resetInternalConfiguration() {
	}

	@Override
	protected void saveInternalConfigurationTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.saveInternalConfigurationTo(config);
	}

	/*
	 * --------------------------------
	 * Setup operations
	 * --------------------------------
	 */

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
