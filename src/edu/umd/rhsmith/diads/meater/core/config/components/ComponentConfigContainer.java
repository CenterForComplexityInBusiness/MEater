package edu.umd.rhsmith.diads.meater.core.config.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentManager;
import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.component.AddComponentOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.component.ListComponentTypesOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.component.ListComponentsOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.component.RemoveComponentOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.component.SelectComponentOperation;

public class ComponentConfigContainer extends ConfigUnit {

	public static final String CKEY_COMPONENTS = "components";

	// mapping of type names to routines for instantiating new configuration
	// types
	private final Map<String, ComponentConfigRegistration<? extends ComponentConfig>> typeRegistrations;

	// component configurations belonging to this module instance
	private final List<ComponentConfig> componentConfigurations;

	public ComponentConfigContainer(String uiName, String uiDescription) {
		super(uiName, uiDescription);

		this.typeRegistrations = new TreeMap<String, ComponentConfigRegistration<? extends ComponentConfig>>();

		this.componentConfigurations = new ArrayList<ComponentConfig>();

		this.registerSetupConsoleOperation(new ListComponentTypesOperation(this));
		this.registerSetupConsoleOperation(new AddComponentOperation(this));
		this.registerSetupConsoleOperation(new ListComponentsOperation(this));
		this.registerSetupConsoleOperation(new SelectComponentOperation(this));
		this.registerSetupConsoleOperation(new RemoveComponentOperation(this));
	}

	public ComponentConfigContainer() {
		this(null, null);
	}

	/*
	 * --------------------------------
	 * General getters/setters
	 * --------------------------------
	 */

	/*
	 * --------------------------------
	 * Component type registrations
	 * --------------------------------
	 */

	public <C extends ComponentConfig> void registerComponentType(Class<C> type) {
		this.registerComponentType(new DefaultComponentConfigRegistration<C>(
				type));
	}

	public void registerComponentType(
			ComponentConfigRegistration<? extends ComponentConfig> registration) {
		this.typeRegistrations.put(registration.getName(), registration);
	}

	public String[] getComponentConfigurationTypeNames() {
		String[] temp = new String[this.typeRegistrations.size()];
		this.typeRegistrations.keySet().toArray(temp);
		return temp;
	}

	public String getComponentTypeDescription(String typeName) {
		ComponentConfigRegistration<? extends ComponentConfig> registration = this.typeRegistrations
				.get(typeName);
		if (registration == null) {
			return null;
		}
		return registration.getDescription();
	}

	public boolean supportsComponentType(String typeName) {
		return this.typeRegistrations.containsKey(typeName);
	}

	/*
	 * --------------------------------
	 * Component configuration instances
	 * --------------------------------
	 */

	public void instantiateComponents(ComponentManager componentManager)
			throws MEaterConfigurationException {
		for (ComponentConfig cc : getComponentConfigurations()) {
			cc.createComponentInstance(componentManager);
		}
	}

	public ComponentConfig addNewComponentConfiguration(String typeName)
			throws ComponentConfigInstantiationException {
		ComponentConfigRegistration<? extends ComponentConfig> registration = this.typeRegistrations
				.get(typeName);

		if (registration == null) {
			throw new ComponentConfigInstantiationException(String.format(
					MSG_ERR_NOTREGISTERED_FMT, typeName, this.getUiName()));
		}

		ComponentConfig config = (ComponentConfig) registration
				.createConfiguration();
		config.setRegisteredTypeName(registration.getName());
		this.componentConfigurations.add(config);
		config.resetConfiguration();

		return config;
	}

	public boolean removeComponentConfiguration(ComponentConfig config) {
		return this.componentConfigurations.remove(config);
	}

	public ComponentConfig getComponentConfigurationByName(String instanceName) {
		for (ComponentConfig m : this.componentConfigurations) {
			if (m.getInstanceName().equals(instanceName)) {
				return m;
			}
		}

		return null;
	}

	public ComponentConfig[] getComponentConfigurations() {
		ComponentConfig[] temp = new ComponentConfig[this.componentConfigurations
				.size()];
		return this.componentConfigurations.toArray(temp);
	}

	public int getNumComponentConfigurations() {
		return this.componentConfigurations.size();
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	@Override
	public void resetConfiguration() {
		this.componentConfigurations.clear();
	}

	@Override
	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// load in components
		this.loadComponentConfigurationsFrom(config);
	}

	@Override
	protected void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// save out components
		this.saveComponentConfigurationsTo(config);
	}

	private void loadComponentConfigurationsFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// go to components section of config
		SubnodeConfiguration componentsConfig;
		try {
			componentsConfig = config.configurationAt(CKEY_COMPONENTS);
		} catch (IllegalArgumentException e) {
			// though not in the apache javadoc, this exception is thrown if no
			// such configuration exists.
			// if it doesn't exist -- no components to load.

			return;
		}

		// pull out all components of types we know how to instantiate
		for (String typeName : this.typeRegistrations.keySet()) {
			List<HierarchicalConfiguration> ccs = componentsConfig
					.configurationsAt(typeName);

			// instantiate + load each one we find
			for (HierarchicalConfiguration cc : ccs) {
				ComponentConfig c;
				try {
					c = this.addNewComponentConfiguration(typeName);
				} catch (ComponentConfigInstantiationException e) {
					throw new MEaterConfigurationException(e.getMessage());
				}

				c.loadConfigurationFrom(cc);
			}
		}
	}

	private void saveComponentConfigurationsTo(HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// create a collection to hold the components section
		Collection<ConfigurationNode> configNodes = new LinkedList<ConfigurationNode>();

		// save all of our components to the section
		for (ComponentConfig c : this.componentConfigurations) {
			// create a configuration for the component + save it, add it
			HierarchicalConfiguration cc = new HierarchicalConfiguration();
			c.saveConfigurationTo(cc);
			cc.getRootNode().setName(c.getRegisteredTypeName());
			configNodes.add(cc.getRootNode());
		}

		// add components section to config
		config.addNodes(CKEY_COMPONENTS, configNodes);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_NOTREGISTERED_FMT = "%s - No such type registered with %s";

}
