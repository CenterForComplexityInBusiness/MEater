package edu.umd.rhsmith.diads.meater.core.config;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.ResetOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesEligible;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesOperation;

public abstract class ConfigUnit {

	private String uiName;
	private String uiDescription;

	private final Collection<SetupConsoleOperation> setupConsoleOperations;

	public ConfigUnit() {
		// treemap so we get name-sorting for free
		this.setupConsoleOperations = new LinkedList<SetupConsoleOperation>();

		// everyone can reset
		this.registerSetupConsoleOperation(new ResetOperation(this));

		// only the eligible have auto-setup
		if (this.getClass().isAnnotationPresent(SetupPropertiesEligible.class)) {
			SetupPropertiesOperation setupProperies = new SetupPropertiesOperation(
					this);
			this.registerSetupConsoleOperation(setupProperies);
		}

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

	protected void registerSetupConsoleOperation(SetupConsoleOperation operation) {
		this.setupConsoleOperations.add(operation);
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

	public abstract void resetConfiguration();

	public final void loadConfigurationFrom(HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// does stuff go here?? maybe later
		this.loadConfigurationPropertiesFrom(config);
	}

	public final void saveConfigurationTo(HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// does stuff go here?? maybe later
		this.saveConfigurationPropertiesTo(config);
	}

	protected abstract void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException;

	protected abstract void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException;

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
