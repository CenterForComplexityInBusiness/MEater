package edu.umd.rhsmith.diads.meater.core.config.container;

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
import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance.AddInstanceOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance.ListInstanceTypesOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance.ListInstancesOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance.RemoveInstanceOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.instance.SelectInstanceOperation;

public class InstanceConfigContainer<I extends InstanceConfig> extends
		ConfigUnit {

	public static final String CKEY_INSTANCES = "instances";

	// mapping of type names to routines for instantiating new configuration
	// types
	private final Map<String, InstanceConfigRegistration<? extends I>> typeRegistrations;

	// configurations belonging to this container
	private final List<I> configUnits;

	public InstanceConfigContainer(String uiName, String uiDescription) {
		super(uiName, uiDescription);

		this.typeRegistrations = new TreeMap<String, InstanceConfigRegistration<? extends I>>();
		this.configUnits = new ArrayList<I>();

		this.registerSetupConsoleOperation(new ListInstanceTypesOperation<I>(
				this));
		this.registerSetupConsoleOperation(new AddInstanceOperation<I>(this));
		this.registerSetupConsoleOperation(new ListInstancesOperation<I>(this));
		this.registerSetupConsoleOperation(new SelectInstanceOperation<I>(this));
		this.registerSetupConsoleOperation(new RemoveInstanceOperation<I>(this));
	}

	public InstanceConfigContainer() {
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

	public <T extends I> void registerConfigType(Class<T> type) {
		this.registerConfigType(new DefaultInstanceConfigRegistration<T>(type));
	}

	public void registerConfigType(
			InstanceConfigRegistration<? extends I> registration) {
		this.typeRegistrations.put(registration.getName(), registration);
	}

	public String[] getInstanceConfigTypeNames() {
		String[] temp = new String[this.typeRegistrations.size()];
		this.typeRegistrations.keySet().toArray(temp);
		return temp;
	}

	public String getConfigTypeDescription(String typeName) {
		InstanceConfigRegistration<?> registration = this.typeRegistrations
				.get(typeName);
		if (registration == null) {
			return null;
		}
		return registration.getDescription();
	}

	public boolean supportsConfigType(String typeName) {
		return this.typeRegistrations.containsKey(typeName);
	}

	/*
	 * --------------------------------
	 * Component configuration instances
	 * --------------------------------
	 */

	public I addNewConfigUnit(String typeName)
			throws ConfigInstantiationException {
		InstanceConfigRegistration<? extends I> registration = this.typeRegistrations
				.get(typeName);

		if (registration == null) {
			throw new ConfigInstantiationException(String.format(
					MSG_ERR_NOTREGISTERED_FMT, typeName, this.getUiName()));
		}

		I config = registration.createConfig();
		config.setRegisteredTypeName(registration.getName());
		this.configUnits.add(config);
		config.resetConfiguration();

		return config;
	}

	public boolean removeInstanceConfig(I config) {
		return this.configUnits.remove(config);
	}

	public I getInstanceConfigByName(String instanceName) {
		for (I m : this.configUnits) {
			if (m.getInstanceName().equals(instanceName)) {
				return m;
			}
		}

		return null;
	}

	public List<I> getInstanceConfigs() {
		return new LinkedList<I>(this.configUnits);
	}

	public int getNumInstanceConfigs() {
		return this.configUnits.size();
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	@Override
	public void resetInternalConfiguration() {
		this.configUnits.clear();
	}

	@Override
	protected void loadInternalConfigurationFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// load in components
		this.loadComponentConfigurationsFrom(config);
	}

	@Override
	protected void saveInternalConfigurationTo(
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
			componentsConfig = config.configurationAt(CKEY_INSTANCES);
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
				I c;
				try {
					c = this.addNewConfigUnit(typeName);
				} catch (ConfigInstantiationException e) {
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
		for (I c : this.configUnits) {
			// create a configuration for the component + save it, add it
			HierarchicalConfiguration cc = new HierarchicalConfiguration();
			c.saveConfigurationTo(cc);
			cc.getRootNode().setName(c.getRegisteredTypeName());
			configNodes.add(cc.getRootNode());
		}

		// add components section to config
		config.addNodes(CKEY_INSTANCES, configNodes);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_NOTREGISTERED_FMT = "%s - No such instance type registered with %s";
}
