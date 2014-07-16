package edu.umd.rhsmith.diads.meater.core.config.container;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.component.RenameInstanceOperation;

public abstract class InstanceConfig extends ConfigUnit {

	public static final String DEFAULT_INSTANCE_NAME = "untitled";
	public static final String CKEY_INSTANCE_NAME = "instanceName";
	public static final String DEFAULT_INSTANCE_TYPE_NAME = "Unit";

	private String instanceTypeName;
	private String instanceName;

	private SetupConsoleOperation creationOperation;

	public InstanceConfig() {
		super();

		this.instanceTypeName = DEFAULT_INSTANCE_TYPE_NAME;
		this.instanceName = DEFAULT_INSTANCE_NAME;

		this.registerSetupConsoleOperation(new RenameInstanceOperation(this));
	}

	/*
	 * --------------------------------
	 * General getters/setters
	 * --------------------------------
	 */

	public final String getRegisteredTypeName() {
		return instanceTypeName;
	}

	final void setRegisteredTypeName(String registeredTypeName) {
		if (registeredTypeName == null) {
			throw new NullPointerException();
		}

		this.instanceTypeName = registeredTypeName;
	}

	public String getInstanceName() {
		return this.instanceName;
	}

	public void setInstanceName(String instanceName) {
		if (instanceName == null) {
			throw new NullPointerException();
		}

		this.instanceName = instanceName;
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

	protected void loadInternalConfigurationFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		this.setInstanceName(config.getString(CKEY_INSTANCE_NAME, this
				.getInstanceName()));
	}

	protected void saveInternalConfigurationTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		config.setProperty(CKEY_INSTANCE_NAME, this.getInstanceName());
	}

	/*
	 * --------------------------------
	 * Setup operations
	 * --------------------------------
	 */

	public final SetupConsoleOperation getCreationSetupConsoleOperation() {
		return creationOperation;
	}

	public final void setCreationSetupConsoleOperation(
			SetupConsoleOperation creationOperation) {
		this.creationOperation = creationOperation;
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
