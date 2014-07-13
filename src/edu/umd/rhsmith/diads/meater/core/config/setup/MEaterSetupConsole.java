package edu.umd.rhsmith.diads.meater.core.config.setup;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.MEaterConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav.ExitOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav.HelpOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav.LoadOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav.NavCloseOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav.SaveAsOperation;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.nav.SaveOperation;
import edu.umd.rhsmith.diads.meater.util.Util;
import edu.umd.rhsmith.diads.meater.util.console.BooleanPrompter;
import edu.umd.rhsmith.diads.meater.util.console.Console;
import edu.umd.rhsmith.diads.meater.util.console.StringPrompter;

public class MEaterSetupConsole implements Runnable {

	public static final String DEFAULT_MEATER_CONFIG_FILENAME = "meater.xml";

	public static void main(String[] args) {
		// interpret args
		String configurationFilename;
		if (args.length == 0) {
			configurationFilename = DEFAULT_MEATER_CONFIG_FILENAME;
		} else {
			configurationFilename = args[1];
		}

		// let's go
		new MEaterSetupConsole(new Console(System.in, System.out, "\t"),
				configurationFilename).run();
	}

	/*
	 * --------------------------------
	 * Actual setup routine
	 * --------------------------------
	 */

	// console used for setup
	private final Console console;

	// filename we're setting up
	private String mainConfigurationFilename;

	// main config we're setting up
	private final MEaterConfig mainConfiguration;

	// stack of config units for 'back' operation
	private final LinkedList<ConfigUnit> navStack;

	// external configs we're setting up
	private final LinkedList<ConfigUnit> externalNavStack;
	private final LinkedList<String> externalFilenameStack;

	// whether we've been asked to exit
	private boolean exit;

	public MEaterSetupConsole(Console console, String mainFilename) {
		this.console = console;
		this.mainConfigurationFilename = mainFilename;

		this.mainConfiguration = new MEaterConfig();
		this.mainConfiguration.resetConfiguration();

		this.navStack = new LinkedList<ConfigUnit>();

		this.externalNavStack = new LinkedList<ConfigUnit>();
		this.externalFilenameStack = new LinkedList<String>();
	}

	public void run() {
		// hello
		this.printWelcome();

		// check for a load file
		checkLoadSaveFile();

		// do setup loop
		this.setupLoop();

		// goodbye
		this.printGoodbye();
	}

	public final MEaterConfig getMainConfiguration() {
		return mainConfiguration;
	}

	/*
	 * --------------------------------
	 * Main setup loop
	 * --------------------------------
	 */

	private void setupLoop() {
		// not exiting
		this.exit = false;

		// start at root module
		this.selectUnit(this.mainConfiguration);

		// go while we have something to setup and are not exiting
		while (this.navStack.size() > 0 && !exit) {
			SetupConsoleOperation doOp = null;

			// divider
			this.console.divide(4);
			this.console.say(MSG_CURR_UNIT_FMT, this.getSelectedUnit()
					.getUiName(), this.getLoadSaveFilename());

			// get the operations supported by the current module
			List<SetupConsoleOperation> ops = this.getSelectedUnit()
					.getSetupOperations();

			// then prompt the user for input
			doOp = promptForOperation(ops, true);

			// if the user gave us an operation, do it
			if (doOp != null) {
				doOp.go(this);
			}
		}
	}

	/*
	 * --------------------------------
	 * User interaction
	 * --------------------------------
	 */

	public Console getConsole() {
		return this.console;
	}

	public void printHelp() {
		// print help if this module is not null
		if (this.getSelectedUnit() != null) {
			// divider
			this.console.divide(4);
			this.console.say(MSG_CURR_UNIT_FMT, this.getSelectedUnit()
					.getUiName(), this.getLoadSaveFilename());
			this.console.say("%s", this.getSelectedUnit().getUiDescription());
			this.console.divide(2);

			// get the operations supported by the current module
			List<SetupConsoleOperation> ops = this.getSelectedUnit()
					.getSetupOperations();

			// list them + the default operations
			this.console.say(MSG_OP_SELECT);
			printOperations(ops, true);
		}
	}

	public SetupConsoleOperation promptForOperation(
			List<SetupConsoleOperation> ops, boolean includeDefaults) {
		SetupConsoleOperation doOp = null;

		// ask user for input; do nothing if they gave none
		String command = this.console.prompt(StringPrompter.PROMPT, false);
		if (command.length() == 0) {
			return null;
		}

		// try to match against a name...
		if (ops != null && doOp == null) {
			for (SetupConsoleOperation op : ops) {
				if (command.equals(op.getShortName())) {
					doOp = op;
				}
			}
		}
		if (includeDefaults && doOp == null) {
			for (SetupConsoleOperation op : DEFAULT_SETUP_OPERATIONS) {
				if (command.equals(op.getShortName())) {
					doOp = op;
				}
			}
		}

		// ...ok, try for a number
		if (doOp == null) {
			try {
				int commandIdx = Integer.parseInt(command);
				if (commandIdx < ops.size()) {
					doOp = ops.get(commandIdx);
				} else if (includeDefaults) {
					doOp = DEFAULT_SETUP_OPERATIONS[commandIdx - ops.size()];
				}
			} catch (NumberFormatException e) {
				// it's okay, just means it didn't match
			} catch (ArrayIndexOutOfBoundsException e) {
				// ditto
			}
		}

		// out of options. let the user know they gave us garbage.
		if (doOp == null) {
			this.console.say(MSG_OP_UNKNOWN_FMT, command,
					HelpOperation.OP_SHORTNAME);
		}

		// if we got nothing, doOp will remain null. otherwise we'll return
		// whatever matched.
		return doOp;
	}

	public void printOperations(List<SetupConsoleOperation> ops,
			boolean includeDefaults) {
		int idx = 0;

		if (ops != null) {
			for (SetupConsoleOperation op : ops) {
				this.printOperation(op, idx);
				++idx;
			}
		}

		if (includeDefaults) {
			this.console.divide(0);
			for (SetupConsoleOperation op : DEFAULT_SETUP_OPERATIONS) {
				this.printOperation(op, idx);
				++idx;
			}
		}
	}

	public void exitSetupLoop() {
		this.console.say(MSG_EXIT_CONFIRM);
		if (this.console.prompt(BooleanPrompter.PROMPT_YESNO, false)) {
			this.exit = true;
		}
	}

	/*
	 * --------------------------------
	 * Load / save
	 * --------------------------------
	 */

	private ConfigUnit getLoadSaveUnit() {
		if (this.externalNavStack.isEmpty()) {
			return this.mainConfiguration;
		}

		return this.externalNavStack.getFirst();
	}

	private String getLoadSaveFilename() {
		if (this.externalFilenameStack.isEmpty()) {
			return this.mainConfigurationFilename;
		}

		return this.externalFilenameStack.getFirst();
	}

	private void setLoadSaveFilename(String filename) {
		if (this.externalNavStack.isEmpty()) {
			this.mainConfigurationFilename = filename;
			return;
		}

		this.externalFilenameStack.set(0, filename);
	}

	private void checkLoadSaveFile() {
		// see if we're working with an existing file
		boolean loadExisting = false;
		File configurationFile = new File(this.getLoadSaveFilename());

		// if we are, ask the user if they want to load it
		if (configurationFile.exists()) {
			this.console.say(MSG_FILE_EXISTS_CHECK_FMT, this
					.getLoadSaveFilename());
			loadExisting = this.console.prompt(BooleanPrompter.PROMPT_YESNO,
					false);
		}

		// if the user wants to load it, do so
		if (loadExisting) {
			try {
				this.loadFile(this.getLoadSaveFilename());
			} catch (ConfigurationException | MEaterConfigurationException e) {
				this.console.error(MSG_ERR_COULDNT_LOAD_FMT, this
						.getLoadSaveFilename(), Util.traceMessage(e));
			}
		}
	}

	public void loadFile(String filename) throws ConfigurationException,
			MEaterConfigurationException {
		XMLConfiguration xml = new XMLConfiguration(filename);
		this.getLoadSaveUnit().loadConfigurationFrom(xml);

		// record that we are now on this file
		this.setLoadSaveFilename(filename);
	}

	public void saveFile() throws ConfigurationException,
			MEaterConfigurationException {
		this.saveFileAs(this.getLoadSaveFilename());
	}

	public void saveFileAs(String filename) throws ConfigurationException,
			MEaterConfigurationException {
		// ask the user if they really want to save over the file
		File configurationFile = new File(filename);
		if (configurationFile.exists()) {
			this.console.say("File \"%s\" already exists - overwrite?",
					filename);
			if (!this.console.prompt(BooleanPrompter.PROMPT_YESNO, false)) {
				return;
			}
		}

		XMLConfiguration xml = new XMLConfiguration();
		this.getLoadSaveUnit().saveConfigurationTo(xml);
		xml.save(filename);

		// record that we are now on this file
		this.setLoadSaveFilename(filename);
	}

	/*
	 * --------------------------------
	 * Navigation
	 * --------------------------------
	 */

	public ConfigUnit getSelectedUnit() {
		if (this.navStack.isEmpty()) {
			return null;
		}

		return this.navStack.getFirst();
	}

	public void selectUnit(ConfigUnit unit) {
		this.navStack.addFirst(unit);
		this.printHelp();
	}

	public void closeSelectedUnit() {
		ConfigUnit u = this.getSelectedUnit();

		// can't close nothing
		if (u == null) {
			return;
		}

		// warn the user before closing a load/savable unit
		if (u == this.getLoadSaveUnit()) {
			this.console.warn(MSG_BACK_CONFIRM_CLOSE_LOADSAVE_FMT, this
					.getLoadSaveUnit().getUiName(), this.getLoadSaveFilename());
			if (!this.console.prompt(BooleanPrompter.PROMPT_YESNO, false)) {
				return;
			}

			if (!this.externalNavStack.isEmpty()) {
				this.externalFilenameStack.removeFirst();
				this.externalNavStack.removeFirst();
			}
		}

		this.navStack.removeFirst();
	}

	public boolean hasPreviousUnit() {
		return this.navStack.size() > 1;
	}

	public void selectExternalUnit(ConfigUnit unit, String filename) {
		this.externalNavStack.addFirst(unit);
		this.externalFilenameStack.addFirst(filename);

		this.checkLoadSaveFile();

		this.selectUnit(unit);
	}

	public ConfigUnit getSelectedExternalUnit() {
		if (this.externalNavStack.isEmpty()) {
			return null;
		}

		return this.externalNavStack.getFirst();
	}

	public String getSelectedExternalUnitFilename() {
		if (this.externalFilenameStack.isEmpty()) {
			return null;
		}

		return this.externalFilenameStack.getFirst();
	}

	/*
	 * --------------------------------
	 * Setup constants
	 * --------------------------------
	 */

	private static final SetupConsoleOperation[] DEFAULT_SETUP_OPERATIONS = {
			// help
			new HelpOperation(),

			// load/save
			new LoadOperation(), new SaveOperation(), new SaveAsOperation(),

			// navigation
			new NavCloseOperation(), new ExitOperation(), };

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_WELCOME = "MEater setup";
	private static final String MSG_ONFILE_FMT = "(Working with configuration file: \"%s\")";
	private static final String MSG_GOODBYE = "Exiting MEater setup";
	private static final String MSG_FILE_EXISTS_CHECK_FMT = "Configuration file \"%s\" exists - load its contents?";
	private static final String MSG_ERR_COULDNT_LOAD_FMT = "Couldn't load contents of configuration file \"%s\": \n%s";
	private static final String MSG_OP_SELECT = "Select an operation: ";
	private static final String MSG_OP_NOSHORT_FMT = "%d) %s";
	private static final String MSG_OP_SHORT_FMT = "%d [%s]) - %s";
	private static final String MSG_OP_UNKNOWN_FMT = "%s: ??? (try \"%s\")";
	private static final String MSG_CURR_UNIT_FMT = "Current configuration unit: %s (%s)";
	private static final String MSG_EXIT_CONFIRM = "Really exit setup?";
	private static final String MSG_BACK_CONFIRM_CLOSE_LOADSAVE_FMT = "Any unsaved changes to %s (%s) will be lost. Are you sure you want to close it?";

	private void printWelcome() {
		this.console.divide(5);
		this.console.say(MSG_WELCOME);
		this.console.say(MSG_ONFILE_FMT, this.mainConfigurationFilename);
		this.console.divide(4);
	}

	private void printGoodbye() {
		this.console.divide(5);
		this.console.say(MSG_GOODBYE);
	}

	private void printOperation(SetupConsoleOperation op, int idx) {
		if (op.getShortName().length() > 0) {
			this.console.say(MSG_OP_SHORT_FMT, idx, op.getShortName(), op
					.getUiName());
		} else {
			this.console.say(MSG_OP_NOSHORT_FMT, idx, op.getShortName(), op
					.getUiName());
		}
	}
}
