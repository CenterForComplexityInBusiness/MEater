package edu.umd.rhsmith.diads.meater.core.app;

import java.util.logging.Level;

public interface MEaterInitializer {

	public long getThreadPoolKeepAliveTimeS();

	public int getMaxThreadPoolSize();

	public int getCoreThreadPoolSize();

	public double getResourceLimitRejectionThreshold();

	public String getMailFromEmail();

	public String getMailToEmail();

	public String getMailSmtp();

	public double getResourceLimitEmailThreshold();

	public int getResourceLimitEmailIntervalS();

	public double getResourceLimitLogThreshold();

	public int getResourceLimitLogIntervalS();

	public Level getLoggingLevel();

	public int getLoggingMaxLogs();

	public int getLoggingMaxSizeMb();

}
