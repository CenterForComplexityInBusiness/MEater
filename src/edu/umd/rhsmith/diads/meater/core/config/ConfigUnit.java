package edu.umd.rhsmith.diads.meater.core.config;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.props.ConfigProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.ResetOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesOperation;

public abstract class ConfigUnit {

	private String uiName;
	private String uiDescription;

	private final List<SetupConsoleOperation> setupConsoleOperations;
	private final List<ConfigProperty<?>> configProperties;

	public ConfigUnit() {
		this.setupConsoleOperations = new LinkedList<SetupConsoleOperation>();
		this.configProperties = new LinkedList<ConfigProperty<?>>();

		// everyone can reset
		this.registerSetupConsoleOperation(new ResetOperation(this));
		// everyone can set up
		this.registerSetupConsoleOperation(new SetupPropertiesOperation(this));

		this.uiName = null;
		this.uiDescription = null;
	}

	public ConfigUnit(String uiName, String uiDescription) {
		this();
		this.uiName = uiName;
		this.uiDescription = uiDescription;
	}

	/*
	 * --------------------------------
	 * General getters/setters
	 * --------------------------------
	 */

	/*
	 * --------------------------------
	 * UI
	 * --------------------------------
	 */

	public String getUiName() {
		return this.uiName;
	}

	public String getUiDescription() {
		return this.uiDescription;
	}

	/*
	 * --------------------------------
	 * Setup operations
	 * --------------------------------
	 */

	public void registerConfigProperty(ConfigProperty<?> prop)
			throws NullPointerException {
		if (prop == null) {
			throw new NullPointerException();
		}

		this.configProperties.add(prop);
	}

	public List<ConfigProperty<?>> getConfigProperties() {
		return new LinkedList<ConfigProperty<?>>(this.configProperties);
	}

	public void registerSetupConsoleOperation(SetupConsoleOperation op)
			throws NullPointerException {
		if (op == null) {
			throw new NullPointerException();
		}

		this.setupConsoleOperations.add(op);
	}

	public List<SetupConsoleOperation> getSetupOperations() {
		return new LinkedList<SetupConsoleOperation>(
				this.setupConsoleOperations);
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	public final void resetConfiguration() {
		this.resetProperties();
		this.resetInternalConfiguration();
	}

	public final void loadConfigurationFrom(HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		this.loadInternalConfigurationFrom(config);
		this.loadPropertiesFrom(config);
	}

	public final void saveConfigurationTo(HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		this.saveInternalConfigurationTo(config);
		this.savePropertiesTo(config);
	}

	private void resetProperties() {
		for (ConfigProperty<?> prop : this.configProperties) {
			prop.reset();
		}
	}

	private void loadPropertiesFrom(HierarchicalConfiguration config) {
		for (ConfigProperty<?> prop : this.configProperties) {
			prop.loadVal(config);
		}
	}

	private void savePropertiesTo(HierarchicalConfiguration config) {
		for (ConfigProperty<?> prop : this.configProperties) {
			prop.saveVal(config);
		}
	}

	protected void resetInternalConfiguration() {
		// override in subclass if you want
	}

	protected void loadInternalConfigurationFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// override in subclass if you want
	}

	protected void saveInternalConfigurationTo(HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// override in subclass if you want
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
