package edu.umd.rhsmith.diads.meater.core.config.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentInitializer;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentManager;
import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.component.RenameComponentOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.ProcessorListOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.SourceListOperation;

public abstract class ComponentConfig extends ConfigUnit implements
		ComponentInitializer {

	public static final String DEFAULT_INSTANCE_NAME = "untitled";
	public static final String CKEY_INSTANCE_NAME = "instanceName";
	public static final String DEFAULT_REGISTERED_TYPE_NAME = "Component";

	private String registeredTypeName;
	private String instanceName;

	private final Map<String, Class<?>> mediaSourceNames;
	private final Map<String, Class<?>> mediaProcessorNames;

	private SetupConsoleOperation creationOperation;

	public ComponentConfig() {
		super();

		this.registeredTypeName = DEFAULT_REGISTERED_TYPE_NAME;
		this.instanceName = DEFAULT_INSTANCE_NAME;

		this.mediaSourceNames = new HashMap<String, Class<?>>();
		this.mediaProcessorNames = new HashMap<String, Class<?>>();

		this.registerSetupConsoleOperation(new RenameComponentOperation(this));
		this.registerSetupConsoleOperation(new SourceListOperation(this));
		this.registerSetupConsoleOperation(new ProcessorListOperation(this));
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

	public final String getRegisteredTypeName() {
		return registeredTypeName;
	}

	final void setRegisteredTypeName(String registeredTypeName) {
		if (registeredTypeName == null) {
			throw new NullPointerException();
		}

		this.registeredTypeName = registeredTypeName;
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

	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		this.setInstanceName(config.getString(CKEY_INSTANCE_NAME, this
				.getInstanceName()));
	}

	@Override
	public void resetConfiguration() {
	}

	protected void saveConfigurationPropertiesTo(
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
