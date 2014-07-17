package edu.umd.rhsmith.diads.meater.core.app;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import edu.umd.rhsmith.diads.meater.core.app.components.ComponentManager;
import edu.umd.rhsmith.diads.meater.core.app.sql.SqlManager;
import edu.umd.rhsmith.diads.meater.util.AlertEmailer;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.meater.util.ControlUnit;
import edu.umd.rhsmith.diads.meater.util.OneLineFormatter;
import edu.umd.rhsmith.diads.meater.util.Util;

public class MEaterMain extends ControlUnit {

	private final String name;
	private final AlertEmailer alertEmailer;
	private final SqlManager sqlManager;
	private final ComponentManager componentManager;

	private final Map<Class<? extends RuntimeModule>, RuntimeModule> runtimeModules;

	public MEaterMain(String name, MEaterInitializer init)
			throws MEaterConfigurationException {
		this.name = name;

		// set up logger
		try {
			Logger logger = Logger.getLogger(this.getName());
			int maxLogSize = init.getLoggingMaxSizeMb() * 10 * Util.MB;
			int maxLogs = init.getLoggingMaxLogs();
			Handler fileHandler = new FileHandler(genLogFileName(this),
					maxLogSize, maxLogs);
			fileHandler.setFormatter(new OneLineFormatter());
			logger.addHandler(fileHandler);
			logger.setLevel(init.getLoggingLevel());
			this.setLogName(this.name);
			this.setLogger(logger);
		} catch (IOException e) {
			throw new LoggerSetupException(e);
		} catch (SecurityException e) {
			throw new LoggerSetupException(e);
		}

		// set up alert emailer
		this.alertEmailer = new AlertEmailer(init.getMailSmtp(), init
				.getMailToEmail(), init.getMailFromEmail());

		// set up environments
		this.sqlManager = new SqlManager(init, this);
		this.componentManager = new ComponentManager(init, this);
		this.runtimeModules = new HashMap<Class<? extends RuntimeModule>, RuntimeModule>();
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	@Override
	protected void doStartupRoutine() throws ControlException {
		for (RuntimeModule m : this.runtimeModules.values()) {
			m.start();
		}
		this.getComponentManager().start();
	}

	@Override
	protected void doShutdownRoutine() {
		this.getComponentManager().stop();
		for (RuntimeModule m : this.runtimeModules.values()) {
			m.stop();
		}
	}

	/*
	 * --------------------------------
	 * General getters/setters
	 * --------------------------------
	 */

	public String getName() {
		return this.name;
	}

	public AlertEmailer getAlertEmailer() {
		return alertEmailer;
	}

	public ComponentManager getComponentManager() {
		return componentManager;
	}

	public SqlManager getSqlManager() {
		return sqlManager;
	}

	/*
	 * --------------------------------
	 * Runtime module system
	 * --------------------------------
	 */

	public void addRuntimeModule(RuntimeModule m) throws IllegalStateException,
			ModuleAlreadyLoadedException {
		synchronized (this.controlLock) {
			this.requireUnStarted();

			if (this.runtimeModules.containsKey(m.getClass())) {
				throw new ModuleAlreadyLoadedException(m.getName());
			}

			this.runtimeModules.put(m.getClass(), m);
			m.setMain(this);
		}
	}

	public <M extends RuntimeModule> M getRuntimeModule(Class<M> moduleClass) {
		return moduleClass.cast(this.runtimeModules.get(moduleClass));
	}

	public boolean hasRuntimeModule(Class<? extends RuntimeModule> moduleClass) {
		return this.runtimeModules.containsKey(moduleClass);
	}

	/*
	 * --------------------------------
	 * Generators for names and such
	 * --------------------------------
	 */

	private static String genLogFileName(final MEaterMain m) {
		return String.format("%s.log", m.getName());
	}

	public static String genName(String configName) {
		return String.format("meater_%s_%dl", configName, new Date().getTime());
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

}
