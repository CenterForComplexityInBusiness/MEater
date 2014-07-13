package edu.umd.rhsmith.diads.meater.core.config;

import java.util.logging.Level;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.MEaterInitializer;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesEligible;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertyTypes;

@SetupPropertiesEligible
public class MEaterGeneralConfig extends ConfigUnit implements MEaterInitializer {
	// values taken from TwEater
	public static final String CKEY_LOGGING_MAX_SIZE_MB = "loggingMaxSizeMb";
	public static final int DEFAULT_LOGGING_MAX_SIZE_MB = 10;
	public static final String CKEY_LOGGING_LEVEL = "loggingLevel";
	public static final String DEFAULT_LOGGING_LEVEL = Level.INFO.toString();
	public static final String CKEY_LOGGING_MAX_LOGS = "loggingMaxLogs";
	public static final int DEFAULT_LOGGING_MAX_LOGS = 10;

	public static final String CKEY_MAIL_STMP = "mailSmtp";
	public static final String DEFAULT_MAIL_STMP = "";
	public static final String CKEY_MAIL_TO_EMAIL = "mailToEmail";
	public static final String DEFAULT_MAIL_TO_EMAIL = "";
	public static final String CKEY_MAIL_FROM_EMAIL = "mailFromEmail";
	public static final String DEFAULT_MAIL_FROM_EMAIL = "";

	public static final String CKEY_RES_LOG_INTERVAL = "resourceLimitLogIntervalS";
	public static final int DEFAULT_RES_LOG_INTERVAL = 600;
	public static final String CKEY_RES_LOG_THRESH = "resourceLimitLogThreshold";
	public static final double DEFAULT_RES_LOG_THRESH = 0.8;
	public static final String CKEY_RES_EMAIL_INTERVAL = "resourceLimitEmailIntervalS";
	public static final int DEFAULT_RES_EMAIL_INTERVAL = 3600;
	public static final String CKEY_RES_EMAIL_THRESH = "resourceLimitEmailThreshold";
	public static final double DEFAULT_RES_EMAIL_THRESH = 0.95;

	private static final String CKEY_RES_REJECTION_THRESH = "resourceLimitRejectionThreshold";
	private static final double DEFAULT_RES_REJECTION_THRESH = 0.95;
	private static final String CKEY_CORE_THREADPOOL_SIZE = "coreThreads";
	private static final int DEFUALT_CORE_THREADPOOL_SIZE = 50;
	private static final String CKEY_MAX_THREADPOOL_SIZE = "maxThreads";
	private static final int DEFAULT_MAX_THREADPOOL_SIZE = 50;
	private static final String CKEY_THREADPOOL_KEEPALIVE_S = "idleTimeout";
	private static final int DEFAULT_THREADPOOL_KEEPALIVE_S = 600;

	public MEaterGeneralConfig() {
		super();
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	// logging info
	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Maximum log size (mb)",
			uiDescription = "Maximum size of a TwEater log file.")
	private int loggingMaxSizeMb;
	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Logging level",
			uiDescription = "Granularity of log messages; valid values (from rarest to most frequent) are: SEVERE, WARNING, INFO, FINE, FINER, FINEST")
	private String loggingLevel;
	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Maximum log files",
			uiDescription = "Maximum number of Tweater log files. After this many logs reach the maximum size, the first file will be overwritten.")
	private int loggingMaxLogs;

	// email doodads
	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "SMTP server from which to send email alerts")
	private String mailSmtp;
	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "Email address to which to send email alerts")
	private String mailToEmail;
	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "Email address from which email alerts will originate")
	private String mailFromEmail;

	// resource limit notifications
	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Resource-limit warning log interval (seconds)")
	private int resourceLimitLogIntervalS;
	@SetupProperty(propertyType = SetupPropertyTypes.DOUBLE,
			uiName = "Resource-limit warning log fraction")
	private double resourceLimitLogThreshold;
	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Resource-limit warning entry interval (seconds)")
	private int resourceLimitEmailIntervalS;
	@SetupProperty(propertyType = SetupPropertyTypes.DOUBLE,
			uiName = "Resource-limit warning email fraction")
	private double resourceLimitEmailThreshold;

	// processing thread-pool
	@SetupProperty(propertyType = SetupPropertyTypes.DOUBLE,
			uiName = "Resource-limit media rejection threshold",
			uiDescription = "Fraction of memory usage above which to reject new media instances")
	private double resourceLimitRejectionThreshold;
	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Processing thread-pool minimum size")
	private int coreThreadPoolSize;
	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Processing thread-pool max size")
	private int maxThreadPoolSize;
	@SetupProperty(propertyType = SetupPropertyTypes.LONG,
			uiName = "Processing thread-pool idle timeout (seconds)")
	private long threadPoolKeepAliveTimeS;

	@Override
	public int getLoggingMaxSizeMb() {
		return this.loggingMaxSizeMb;
	}

	@Override
	public int getLoggingMaxLogs() {
		return this.loggingMaxLogs;
	}

	@Override
	public Level getLoggingLevel() {
		try {
			return Level.parse(this.loggingLevel);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public int getResourceLimitLogIntervalS() {
		return resourceLimitLogIntervalS;
	}

	@Override
	public double getResourceLimitLogThreshold() {
		return resourceLimitLogThreshold;
	}

	@Override
	public int getResourceLimitEmailIntervalS() {
		return resourceLimitEmailIntervalS;
	}

	@Override
	public double getResourceLimitEmailThreshold() {
		return resourceLimitEmailThreshold;
	}

	@Override
	public String getMailSmtp() {
		return this.mailSmtp;
	}

	@Override
	public String getMailToEmail() {
		return this.mailToEmail;
	}

	@Override
	public String getMailFromEmail() {
		return this.mailFromEmail;
	}

	@Override
	public double getResourceLimitRejectionThreshold() {
		return this.resourceLimitRejectionThreshold;
	}

	@Override
	public int getCoreThreadPoolSize() {
		return this.coreThreadPoolSize;
	}

	@Override
	public int getMaxThreadPoolSize() {
		return this.maxThreadPoolSize;
	}

	@Override
	public long getThreadPoolKeepAliveTimeS() {
		return this.threadPoolKeepAliveTimeS;
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	@Override
	public void resetConfiguration() {
		this.loggingMaxSizeMb = DEFAULT_LOGGING_MAX_SIZE_MB;
		this.loggingLevel = DEFAULT_LOGGING_LEVEL;
		this.loggingMaxLogs = DEFAULT_LOGGING_MAX_LOGS;

		this.mailSmtp = DEFAULT_MAIL_STMP;
		this.mailToEmail = DEFAULT_MAIL_TO_EMAIL;
		this.mailFromEmail = DEFAULT_MAIL_FROM_EMAIL;

		this.resourceLimitLogIntervalS = DEFAULT_RES_LOG_INTERVAL;
		this.resourceLimitEmailIntervalS = DEFAULT_RES_EMAIL_INTERVAL;
		this.resourceLimitLogThreshold = DEFAULT_RES_LOG_THRESH;
		this.resourceLimitEmailThreshold = DEFAULT_RES_EMAIL_THRESH;

		this.resourceLimitRejectionThreshold = DEFAULT_RES_REJECTION_THRESH;
		this.coreThreadPoolSize = DEFUALT_CORE_THREADPOOL_SIZE;
		this.maxThreadPoolSize = DEFAULT_MAX_THREADPOOL_SIZE;
		this.threadPoolKeepAliveTimeS = DEFAULT_THREADPOOL_KEEPALIVE_S;
	}

	@Override
	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		this.loggingMaxSizeMb = config.getInt(CKEY_LOGGING_MAX_SIZE_MB,
				this.loggingMaxSizeMb);
		this.loggingLevel = config.getString(CKEY_LOGGING_LEVEL,
				this.loggingLevel);
		this.loggingMaxLogs = config.getInt(CKEY_LOGGING_MAX_LOGS,
				this.loggingMaxLogs);

		this.mailSmtp = config.getString(CKEY_MAIL_STMP, this.mailSmtp);
		this.mailToEmail = config.getString(CKEY_MAIL_TO_EMAIL,
				DEFAULT_MAIL_TO_EMAIL);
		this.mailFromEmail = config.getString(CKEY_MAIL_FROM_EMAIL,
				this.mailFromEmail);

		this.resourceLimitLogIntervalS = config.getInt(CKEY_RES_LOG_INTERVAL,
				this.resourceLimitLogIntervalS);
		this.resourceLimitEmailIntervalS = config.getInt(
				CKEY_RES_EMAIL_INTERVAL, this.resourceLimitEmailIntervalS);
		this.resourceLimitLogThreshold = config.getDouble(CKEY_RES_LOG_THRESH,
				this.resourceLimitLogThreshold);
		this.resourceLimitEmailThreshold = config.getDouble(
				CKEY_RES_EMAIL_THRESH, this.resourceLimitEmailThreshold);

		this.resourceLimitRejectionThreshold = config
				.getDouble(CKEY_RES_REJECTION_THRESH,
						this.resourceLimitRejectionThreshold);
		this.coreThreadPoolSize = config.getInt(CKEY_CORE_THREADPOOL_SIZE,
				this.coreThreadPoolSize);
		this.maxThreadPoolSize = config.getInt(CKEY_MAX_THREADPOOL_SIZE,
				this.maxThreadPoolSize);
		this.threadPoolKeepAliveTimeS = config.getLong(
				CKEY_THREADPOOL_KEEPALIVE_S, this.threadPoolKeepAliveTimeS);
	}

	@Override
	protected void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		config.setProperty(CKEY_LOGGING_MAX_SIZE_MB, this.loggingMaxSizeMb);
		config.setProperty(CKEY_LOGGING_LEVEL, this.loggingLevel);
		config.setProperty(CKEY_LOGGING_MAX_LOGS, this.loggingMaxLogs);

		config.setProperty(CKEY_MAIL_STMP, this.mailSmtp);
		config.setProperty(CKEY_MAIL_TO_EMAIL, this.mailToEmail);
		config.setProperty(CKEY_MAIL_FROM_EMAIL, this.mailFromEmail);

		config.setProperty(CKEY_RES_LOG_INTERVAL,
				this.resourceLimitLogIntervalS);
		config.setProperty(CKEY_RES_EMAIL_INTERVAL,
				this.resourceLimitEmailIntervalS);
		config.setProperty(CKEY_RES_LOG_THRESH, this.resourceLimitLogThreshold);
		config.setProperty(CKEY_RES_EMAIL_THRESH,
				this.resourceLimitEmailThreshold);

		config.setProperty(CKEY_RES_REJECTION_THRESH,
				this.resourceLimitRejectionThreshold);
		config.setProperty(CKEY_CORE_THREADPOOL_SIZE, this.coreThreadPoolSize);
		config.setProperty(CKEY_MAX_THREADPOOL_SIZE, this.maxThreadPoolSize);
		config.setProperty(CKEY_THREADPOOL_KEEPALIVE_S,
				this.threadPoolKeepAliveTimeS);
	}

	/*
	 * --------------------------------
	 * UI
	 * --------------------------------
	 */

	@Override
	public String getUiName() {
		return MSG_UINAME;
	}

	@Override
	public String getUiDescription() {
		return MSG_UIDESC;
	}

	/*
	 * --------------------------------
	 * Message strings
	 * --------------------------------
	 */

	private static final String MSG_UINAME = "MEater Main - General Settings";
	private static final String MSG_UIDESC = "This contains general settings for the MEater "
			+ "application, such as logging and email-alerts";
}
