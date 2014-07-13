package edu.umd.rhsmith.diads.meater.core.config.setup.ops.media.path;

import java.util.Map;
import java.util.TreeMap;

import edu.umd.rhsmith.diads.meater.core.config.ConfigModule;
import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaPathConfig;
import edu.umd.rhsmith.diads.meater.core.config.components.media.MediaRegistration;
import edu.umd.rhsmith.diads.meater.core.config.setup.MEaterSetupConsole;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.SetupConsoleOperation;

public class PathSetMediaOperation extends SetupConsoleOperation {
	private static final String OP_SHORTNAME = "set-path-media-type";
	private static final String OP_UINAME = "Set the media type this path will operate on";
	private MediaPathConfig owner;

	public PathSetMediaOperation(MediaPathConfig owner) {
		super(OP_UINAME, OP_SHORTNAME);

		this.owner = owner;
	}

	@Override
	public void go(MEaterSetupConsole setup) {
		setup.getConsole().say(MSG_HEADER);

		// build list of all currently registered media types
		Map<String, MediaRegistration<?>> regs = new TreeMap<String, MediaRegistration<?>>();
		for (ConfigModule module : setup.getMainConfiguration()
				.getModules()) {
			for (MediaRegistration<?> reg : module.getRegisteredMediaTypes()) {
				regs.put(reg.getMediaName(), reg);
			}
		}

		// give them to the user
		setup.getConsole().pushIndent(1);
		for (MediaRegistration<?> reg : regs.values()) {
			setup.getConsole().say("'%s': %s", reg.getMediaName(),
					reg.getMediaClass());
		}
		setup.getConsole().popIndent();

		// ask user for their life-altering choice
		setup.getConsole().say(MSG_PROMPT_NAME);
		String name = setup.getConsole().prompt(false);
		if (regs.containsKey(name)) {
			this.owner.setMediaClass(regs.get(name).getMediaClass());
			setup.getConsole().say(MSG_SUCCESS_FMT, name);
		} else {
			setup.getConsole().say(MSG_FAILURE_FMT, name);
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_HEADER = "Registered media types: ";
	private static final String MSG_PROMPT_NAME = "Enter the name of the media type to use.";
	private static final String MSG_SUCCESS_FMT = "Media path switched to type '%s'.";
	private static final String MSG_FAILURE_FMT = "No registered media type '%s'.";
}
