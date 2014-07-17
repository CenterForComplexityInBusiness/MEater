package edu.umd.rhsmith.diads.meater.core.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.MEaterException;
import edu.umd.rhsmith.diads.meater.core.app.MEaterMain;
import edu.umd.rhsmith.diads.meater.core.app.ModuleAlreadyLoadedException;
import edu.umd.rhsmith.diads.meater.core.app.ModuleInstantiationException;
import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.container.InstanceConfigContainer;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.module.AddModuleOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.module.ListModulesOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.module.RemoveModuleOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.module.SelectModuleOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav.NavToOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.sql.EditSqlOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesOperation;
import edu.umd.rhsmith.diads.meater.util.Util;

public class MEaterConfig extends ConfigUnit {

	/*
	 * --------------------------------
	 * entry point
	 * --------------------------------
	 */

	public static void main(String[] args) {
		// interpret args
		String configurationFilename;
		if (args.length == 0) {
			configurationFilename = MEaterSetupConsole.DEFAULT_MEATER_CONFIG_FILENAME;
		} else {
			configurationFilename = args[1];
		}

		// let's go
		MEaterMain main = null;

		// create an application instance
		try {
			main = mainFromConfigurationFile(configurationFilename);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// start the instance and then wait for it to stop
		// FIXME handle error conditions in startup
		try {
			main.start();
			main.awaitStopFinished();
		} catch (Exception e) {
			main.logSevere(Util.traceMessage(e));
			System.exit(-1);
		}
	}

	public static MEaterMain mainFromConfigurationFile(String filename)
			throws ConfigurationException, MEaterException {
		XMLConfiguration xml = new XMLConfiguration(filename);
		MEaterConfig config = new MEaterConfig();
		config.resetInternalConfiguration();
		config.loadConfigurationFrom(xml);
		return config.createMEaterMain(filename);
	}

	/*
	 * --------------------------------
	 * actual class
	 * --------------------------------
	 */

	public static final String UNAME_MEATER = "meater";
	public static final String CKEY_MODULE_CLASSNAME = "moduleClass";
	public static final String CKEY_MODULES = "modules";

	private final MEaterGeneralConfig generalSettings;
	private final Map<String, ConfigModule> modules;
	private final InstanceConfigContainer<MediaPathConfig> pathContainer;

	public MEaterConfig() {
		super();

		this.generalSettings = new MEaterGeneralConfig();
		this.modules = new TreeMap<String, ConfigModule>();

		// media paths
		this.pathContainer = new InstanceConfigContainer<MediaPathConfig>(
				UINAME_PATHS, UIDESC_PATHS);
		this.pathContainer.registerConfigType(MediaPathConfig.class);

		// nav to general
		this.registerSetupConsoleOperation(new SetupPropertiesOperation(
				OP_UINAME_GENERAL, OP_SHORTNAME_GENERAL, this.generalSettings));

		// nav to media paths
		this.registerSetupConsoleOperation(new NavToOperation(OP_UINAME_PATHS,
				OP_SHORTNAME_PATHS, this.pathContainer));

		// edit sql things
		this.registerSetupConsoleOperation(new EditSqlOperation());

		// module add, selection, etc
		this.registerSetupConsoleOperation(new AddModuleOperation(this));
		this.registerSetupConsoleOperation(new ListModulesOperation(this));
		this.registerSetupConsoleOperation(new SelectModuleOperation(this));
		this.registerSetupConsoleOperation(new RemoveModuleOperation(this));
	}

	public MEaterGeneralConfig getGeneralSettings() {
		return this.generalSettings;
	}

	public InstanceConfigContainer<MediaPathConfig> getPathContainer() {
		return pathContainer;
	}

	public MEaterMain createMEaterMain(String name) throws MEaterException {
		MEaterMain main = new MEaterMain(MEaterMain.genName(name), this
				.getGeneralSettings());

		for (ConfigModule module : this.getModules()) {
			module.addTo(main);
		}
		for (MediaPathConfig cc : this.pathContainer.getInstanceConfigs()) {
			cc.createComponentInstance(main.getComponentManager());
		}

		return main;
	}

	/*
	 * --------------------------------
	 * UI
	 * --------------------------------
	 */

	@Override
	public String getUiName() {
		return UINAME;
	}

	@Override
	public String getUiDescription() {
		return UIDESC;
	}

	/*
	 * --------------------------------
	 * Modules
	 * --------------------------------
	 */

	public ConfigModule addModule(String className)
			throws ModuleInstantiationException, ModuleAlreadyLoadedException {
		ConfigModule module = ConfigModule.forName(className);

		// can't add if we already have one by the same name
		if (this.modules.containsKey(module.getModuleName())) {
			throw new ModuleAlreadyLoadedException(module.getModuleName());
		}

		this.modules.put(module.getModuleName(), module);
		module.resetInternalConfiguration();

		return module;
	}

	public void removeModule(ConfigModule module) {
		this.modules.remove(module.getModuleName());
	}

	public ConfigModule getModuleByName(String name) {
		return this.modules.get(name);
	}

	public Set<ConfigModule> getModules() {
		return new HashSet<ConfigModule>(this.modules.values());
	}

	public int getNumModules() {
		return this.modules.size();
	}

	private void loadModuleClassesFrom(HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// grab all the module class names and load corresponding modules
		String[] classes = config.getStringArray(CKEY_MODULE_CLASSNAME);
		for (String className : classes) {
			try {
				this.addModule(className);
			} catch (IllegalStateException e) {
				// this doesn't mean anything bad, we just already had the
				// module loaded
			} catch (ModuleInstantiationException e) {
				// this is actually bad, we got a bogus class name
				throw new MEaterConfigurationException(
						"Couldn't instantiate module");
			}
		}
	}

	private void saveModuleClassesTo(HierarchicalConfiguration config) {
		// grab all the modules and add their class names to the config
		for (ConfigModule m : this.modules.values()) {
			config.addProperty(CKEY_MODULE_CLASSNAME, m.getClass().getName());
		}
	}

	private void loadModulesFrom(HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// go to modules section of config
		HierarchicalConfiguration modulesConfig;
		try {
			modulesConfig = config.configurationAt(CKEY_MODULES);
		} catch (IllegalArgumentException e) {
			// though not in the apache javadoc, this exception is thrown if no
			// such configuration exists.
			// if it doesn't exist -- no modules to load.
			return;
		}

		// load each module we find
		for (Entry<String, ConfigModule> entry : this.modules.entrySet()) {
			List<HierarchicalConfiguration> mcs = modulesConfig
					.configurationsAt(entry.getKey());

			for (HierarchicalConfiguration mc : mcs) {
				entry.getValue().loadConfigurationFrom(mc);
			}
		}
	}

	private void saveModulesTo(HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		// create a collection to hold the modules section
		Collection<ConfigurationNode> moduleNodes = new LinkedList<ConfigurationNode>();

		// save all of our modules to the section
		for (ConfigModule m : this.modules.values()) {
			// create a configuration for the module + save it, add it
			HierarchicalConfiguration mc = new HierarchicalConfiguration();
			m.saveConfigurationTo(mc);
			mc.getRootNode().setName(m.getModuleName());
			moduleNodes.add(mc.getRootNode());
		}

		// add modules section to config
		config.addNodes(CKEY_MODULES, moduleNodes);
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	@Override
	public void resetInternalConfiguration() {
		// do general settings
		this.generalSettings.resetInternalConfiguration();

		// do all modules
		for (ConfigModule m : this.modules.values()) {
			m.resetInternalConfiguration();
		}
	}

	@Override
	protected void loadInternalConfigurationFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		this.loadModuleClassesFrom(config);
		this.loadModulesFrom(config);
		
		this.generalSettings.loadConfigurationFrom(config);
		this.pathContainer.loadConfigurationFrom(config);
	}

	@Override
	protected void saveInternalConfigurationTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		this.saveModuleClassesTo(config);
		this.saveModulesTo(config);

		this.generalSettings.saveConfigurationTo(config);
		this.pathContainer.saveConfigurationTo(config);
	}

	/*
	 * --------------------------------
	 * Message strings
	 * --------------------------------
	 */

	private static final String UINAME = "MEater Main";
	private static final String UIDESC = "This is the main configuration "
			+ "element of MEater. It contains core application information such as "
			+ "loaded modules and logging settings.";

	private static final String OP_UINAME_PATHS = "Set up media-processing paths";
	private static final String OP_UINAME_GENERAL = "Set up general MEater configuration settings";
	private static final String OP_SHORTNAME_PATHS = "setup-paths";
	private static final String OP_SHORTNAME_GENERAL = "setup-general";

	private static final String UINAME_PATHS = "Media-processing paths";
	private static final String UIDESC_PATHS = "This unit holds the media-processing paths used in this MEater configuration.";
}
