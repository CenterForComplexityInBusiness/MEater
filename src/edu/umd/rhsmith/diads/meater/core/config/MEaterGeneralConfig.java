package edu.umd.rhsmith.diads.meater.core.config;

import java.util.logging.Level;

import edu.umd.rhsmith.diads.meater.core.app.MEaterInitializer;
import edu.umd.rhsmith.diads.meater.core.config.props.DoubleProperty;
import edu.umd.rhsmith.diads.meater.core.config.props.IntProperty;
import edu.umd.rhsmith.diads.meater.core.config.props.LongProperty;
import edu.umd.rhsmith.diads.meater.core.config.props.StringProperty;

public class MEaterGeneralConfig extends ConfigUnit implements
		MEaterInitializer {

	public MEaterGeneralConfig() {
		super();

		this.registerConfigProperty(loggingLevel);
		this.registerConfigProperty(loggingMaxLogs);
		this.registerConfigProperty(loggingMaxSizeMb);

		this.registerConfigProperty(mailSmtp);
		this.registerConfigProperty(mailFromEmail);
		this.registerConfigProperty(mailToEmail);

		this.registerConfigProperty(resourceLimitLogThreshold);
		this.registerConfigProperty(resourceLimitLogIntervalS);
		this.registerConfigProperty(resourceLimitEmailThreshold);
		this.registerConfigProperty(resourceLimitEmailIntervalS);

		this.registerConfigProperty(threadPoolCoreSize);
		this.registerConfigProperty(threadPoolMinSize);
		this.registerConfigProperty(threadPoolKeepAliveTimeS);
		this.registerConfigProperty(resourceLimitRejectionThreshold);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	// logging info
	private static final String CKEY_LOGGING_MAX_SIZE_MB = "loggingMaxSizeMb";
	private static final int DEFAULT_LOGGING_MAX_SIZE_MB = 10;
	private static final String UINAME_LOGGING_MAX_SIZE_MB = "Maximum log size (mb)";
	private static final String UIDESC_LOGGING_MAX_SIZE_MB = "Maximum size of a TwEater log file.";
	private final IntProperty loggingMaxSizeMb = new IntProperty(
			CKEY_LOGGING_MAX_SIZE_MB, DEFAULT_LOGGING_MAX_SIZE_MB,
			UINAME_LOGGING_MAX_SIZE_MB, UIDESC_LOGGING_MAX_SIZE_MB);
	private static final String CKEY_LOGGING_LEVEL = "loggingLevel";
	private static final String DEFAULT_LOGGING_LEVEL = Level.INFO.toString();
	private static final String UINAME_LOGGING_LEVE = "Logging level";
	private static final String UIDESC_LOGGING_LEVE = "Granularity of log messages; valid values (from rarest to most frequent) are: SEVERE, WARNING, INFO, FINE, FINER, FINEST";
	private final StringProperty loggingLevel = new StringProperty(
			CKEY_LOGGING_LEVEL, DEFAULT_LOGGING_LEVEL, UINAME_LOGGING_LEVE,
			UIDESC_LOGGING_LEVE);
	private static final String CKEY_LOGGING_MAX_LOGS = "loggingMaxLogs";
	private static final int DEFAULT_LOGGING_MAX_LOGS = 10;
	private static final String UINAME_LOGGING_MAX_LOGS = "Maximum log files";
	private static final String UIDESC_LOGGING_MAX_LOGS = "Maximum number of Tweater log files. After this many logs reach the maximum size, the first file will be overwritten.";
	private final IntProperty loggingMaxLogs = new IntProperty(
			CKEY_LOGGING_MAX_LOGS, DEFAULT_LOGGING_MAX_LOGS,
			UINAME_LOGGING_MAX_LOGS, UIDESC_LOGGING_MAX_LOGS);

	// email doodads
	private static final String CKEY_MAIL_STMP = "mailSmtp";
	private static final String DEFAULT_MAIL_STMP = "";
	private static final String UINAME_MAIL_FROM_EMAIL = "Mail-from";
	private static final String UIDESC_MAIL_FROM_EMAIL = "Email address from which email alerts will originate";
	private final StringProperty mailSmtp = new StringProperty(CKEY_MAIL_STMP,
			DEFAULT_MAIL_STMP, UINAME_MAIL_STMP, UIDESC_MAIL_STMP);
	private static final String CKEY_MAIL_TO_EMAIL = "mailToEmail";
	private static final String DEFAULT_MAIL_TO_EMAIL = "";
	private static final String UINAME_MAIL_TO_EMAIL = "Mail-to";
	private static final String UIDESC_MAIL_TO_EMAIL = "Email address to which to send email alerts";
	private final StringProperty mailToEmail = new StringProperty(
			CKEY_MAIL_TO_EMAIL, DEFAULT_MAIL_TO_EMAIL, UINAME_MAIL_TO_EMAIL,
			UIDESC_MAIL_TO_EMAIL);
	private static final String CKEY_MAIL_FROM_EMAIL = "mailFromEmail";
	private static final String DEFAULT_MAIL_FROM_EMAIL = "";
	private static final String UINAME_MAIL_STMP = "Mail SMTP";
	private static final String UIDESC_MAIL_STMP = "SMTP server from which to send email alerts";
	private final StringProperty mailFromEmail = new StringProperty(
			CKEY_MAIL_FROM_EMAIL, DEFAULT_MAIL_FROM_EMAIL,
			UINAME_MAIL_FROM_EMAIL, UIDESC_MAIL_FROM_EMAIL);

	// resource limit notifications
	private static final String CKEY_RES_LOG_INTERVAL_S = "resourceLimitLogIntervalS";
	private static final int DEFAULT_RES_LOG_INTERVAL_S = 600;
	private static final String UINAME_RES_LOG_INTERVAL_S = "Resource-limit warning log interval (seconds)";
	private static final String UIDESC_RES_LOG_INTERVAL_S = "";
	private final IntProperty resourceLimitLogIntervalS = new IntProperty(
			CKEY_RES_LOG_INTERVAL_S, DEFAULT_RES_LOG_INTERVAL_S,
			UINAME_RES_LOG_INTERVAL_S, UIDESC_RES_LOG_INTERVAL_S);
	private static final String CKEY_RES_LOG_THRESH = "resourceLimitLogThreshold";
	private static final double DEFAULT_RES_LOG_THRESH = 0.8;
	private static final String UINAME_RES_LOG_THRESH = "Resource-limit warning log fraction";
	private static final String UIDESC_RES_LOG_THRESH = "";
	private final DoubleProperty resourceLimitLogThreshold = new DoubleProperty(
			CKEY_RES_LOG_THRESH, DEFAULT_RES_LOG_THRESH, UINAME_RES_LOG_THRESH,
			UIDESC_RES_LOG_THRESH);
	private static final String CKEY_RES_EMAIL_INTERVAL = "resourceLimitEmailIntervalS";
	private static final int DEFAULT_RES_EMAIL_INTERVAL = 3600;
	private static final String UINAME_RES_EMAIL_INTERVAL = "Resource-limit warning entry interval (seconds)";
	private static final String UIDESC_RES_EMAIL_INTERVAL = "";
	private final IntProperty resourceLimitEmailIntervalS = new IntProperty(
			CKEY_RES_EMAIL_INTERVAL, DEFAULT_RES_EMAIL_INTERVAL,
			UINAME_RES_EMAIL_INTERVAL, UIDESC_RES_EMAIL_INTERVAL);
	private static final String CKEY_RES_EMAIL_THRESH = "resourceLimitEmailThreshold";
	private static final double DEFAULT_RES_EMAIL_THRESH = 0.95;
	private static final String UINAME_RES_EMAIL_THRESH = "Resource-limit warning email fraction";
	private static final String UIDESC_RES_EMAIL_THRESH = "";
	private final DoubleProperty resourceLimitEmailThreshold = new DoubleProperty(
			CKEY_RES_EMAIL_THRESH, DEFAULT_RES_EMAIL_THRESH,
			UINAME_RES_EMAIL_THRESH, UIDESC_RES_EMAIL_THRESH);

	// processing thread-pool
	private static final String CKEY_RES_REJECTION_THRESH = "resourceLimitRejectionThreshold";
	private static final double DEFAULT_RES_REJECTION_THRESH = 0.95;
	private static final String UINAME_RES_REJECTION_THRESH = "Resource-limit media rejection threshold";
	private static final String UIDESC_RES_REJECTION_THRESH = "Fraction of memory usage above which to reject new media instances";
	private final DoubleProperty resourceLimitRejectionThreshold = new DoubleProperty(
			CKEY_RES_REJECTION_THRESH, DEFAULT_RES_REJECTION_THRESH,
			UINAME_RES_REJECTION_THRESH, UIDESC_RES_REJECTION_THRESH);
	private static final String CKEY_CORE_THREADPOOL_SIZE = "coreThreads";
	private static final int DEFAULT_CORE_THREADPOOL_SIZE = 50;
	private static final String UINAME_CORE_THREADPOOL_SIZE = "Processing thread-pool minimum size";
	private static final String UIDESC_CORE_THREADPOOL_SIZE = "";
	private final IntProperty threadPoolCoreSize = new IntProperty(
			CKEY_CORE_THREADPOOL_SIZE, DEFAULT_CORE_THREADPOOL_SIZE,
			UINAME_CORE_THREADPOOL_SIZE, UIDESC_CORE_THREADPOOL_SIZE);
	private static final String CKEY_MAX_THREADPOOL_SIZE = "maxThreads";
	private static final int DEFAULT_MAX_THREADPOOL_SIZE = 50;
	private static final String UINAME_MAX_THREADPOOL_SIZE = "Processing thread-pool max size";
	private static final String UIDESC_MAX_THREADPOOL_SIZE = "";
	private final IntProperty threadPoolMinSize = new IntProperty(
			CKEY_MAX_THREADPOOL_SIZE, DEFAULT_MAX_THREADPOOL_SIZE,
			UINAME_MAX_THREADPOOL_SIZE, UIDESC_MAX_THREADPOOL_SIZE);
	private static final String CKEY_THREADPOOL_KEEPALIVE_S = "idleTimeout";
	private static final long DEFAULT_THREADPOOL_KEEPALIVE_S = 600;
	private static final String UINAME_THREADPOOL_KEEPALIVE_S = "Processing thread-pool idle timeout (seconds)";
	private static final String UIDESC_THREADPOOL_KEEPALIVE_S = "";
	private final LongProperty threadPoolKeepAliveTimeS = new LongProperty(
			CKEY_THREADPOOL_KEEPALIVE_S, DEFAULT_THREADPOOL_KEEPALIVE_S,
			UINAME_THREADPOOL_KEEPALIVE_S, UIDESC_THREADPOOL_KEEPALIVE_S);

	@Override
	public int getLoggingMaxSizeMb() {
		return this.loggingMaxSizeMb.getVal();
	}

	@Override
	public int getLoggingMaxLogs() {
		return this.loggingMaxLogs.getVal();
	}

	@Override
	public Level getLoggingLevel() {
		try {
			return Level.parse(this.loggingLevel.getVal());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public int getResourceLimitLogIntervalS() {
		return resourceLimitLogIntervalS.getVal();
	}

	@Override
	public double getResourceLimitLogThreshold() {
		return resourceLimitLogThreshold.getVal();
	}

	@Override
	public int getResourceLimitEmailIntervalS() {
		return resourceLimitEmailIntervalS.getVal();
	}

	@Override
	public double getResourceLimitEmailThreshold() {
		return resourceLimitEmailThreshold.getVal();
	}

	@Override
	public String getMailSmtp() {
		return this.mailSmtp.getVal();
	}

	@Override
	public String getMailToEmail() {
		return this.mailToEmail.getVal();
	}

	@Override
	public String getMailFromEmail() {
		return this.mailFromEmail.getVal();
	}

	@Override
	public double getResourceLimitRejectionThreshold() {
		return this.resourceLimitRejectionThreshold.getVal();
	}

	@Override
	public int getCoreThreadPoolSize() {
		return this.threadPoolCoreSize.getVal();
	}

	@Override
	public int getMaxThreadPoolSize() {
		return this.threadPoolMinSize.getVal();
	}

	@Override
	public long getThreadPoolKeepAliveTimeS() {
		return this.threadPoolKeepAliveTimeS.getVal();
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

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
	 * Message strings
	 * --------------------------------
	 */

	private static final String UINAME = "MEater Main - General Settings";
	private static final String UIDESC = "This contains general settings for the MEater "
			+ "application, such as logging and email-alerts";
}
