package edu.umd.rhsmith.diads.meater.util;

import java.util.logging.Logger;

public class LogUnit {

	private Logger logger;
	private String logName;

	public Logger getLogger() {
		return this.logger;
	}

	public String getLogName() {
		return this.logName;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public void logSevere(String msg, Object... args) {
		if (this.getLogger() != null) {
			this.getLogger().severe(this.messageString(msg, args));
		}
	}

	public void logWarning(String msg, Object... args) {
		if (this.getLogger() != null) {
			this.getLogger().warning(this.messageString(msg, args));
		}
	}

	public void logInfo(String msg, Object... args) {
		if (this.getLogger() != null) {
			this.getLogger().info(this.messageString(msg, args));
		}
	}

	public void logFine(String msg, Object... args) {
		if (this.getLogger() != null) {
			this.getLogger().fine(this.messageString(msg, args));
		}
	}

	public void logFiner(String msg, Object... args) {
		if (this.getLogger() != null) {
			this.getLogger().finer(this.messageString(msg, args));
		}
	}

	public void logFinest(String msg, Object... args) {
		if (this.getLogger() != null) {
			this.getLogger().finest(this.messageString(msg, args));
		}
	}

	public void logSevere(String msg) {
		if (this.getLogger() != null) {
			this.getLogger().severe(this.messageString(msg));
		}
	}

	public void logWarning(String msg) {
		if (this.getLogger() != null) {
			this.getLogger().warning(this.messageString(msg));
		}
	}

	public void logInfo(String msg) {
		if (this.getLogger() != null) {
			this.getLogger().info(this.messageString(msg));
		}
	}

	public void logFine(String msg) {
		if (this.getLogger() != null) {
			this.getLogger().fine(this.messageString(msg));
		}
	}

	public void logFiner(String msg) {
		if (this.getLogger() != null) {
			this.getLogger().finer(this.messageString(msg));
		}
	}

	public void logFinest(String msg) {
		if (this.getLogger() != null) {
			this.getLogger().finest(this.messageString(msg));
		}
	}

	public String messageString(String fmt, Object... args) {
		// prepend component identifier before actually formatting
		if (this.getLogName() != null) {
			fmt = String.format("'%s': %s", this.getLogName(), fmt);
		}
		return String.format(fmt, args);
	}

	public String messageString(String fmt) {
		// prepend component identifier before actually formatting
		if (this.getLogName() != null) {
			fmt = String.format("'%s': %s", this.getLogName(), fmt);
		}
		return fmt;
	}
}