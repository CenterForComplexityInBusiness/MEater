package edu.umd.rhsmith.diads.meater.core.config.sql;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.sql.SqlInfo;
import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesEligible;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertyTypes;

@SetupPropertiesEligible
public class SqlConfig extends ConfigUnit implements SqlInfo {

	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "Host name",
			uiDescription = "The host name of the server to connect to")
	private String hostname;
	public static final String CKEY_HOSTNAME = "hostname";

	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "Database name",
			uiDescription = "The name of the database on the host machine to connect to")
	private String dbName;
	public static final String CKEY_DBNAME = "dbName";

	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "Username",
			uiDescription = "The username to use when connecting to the database")
	private String user;
	public static final String CKEY_USER = "user";

	// FIXME passwords really, really should be done in a more secure way. i
	// think so, at least.
	@SetupProperty(propertyType = SetupPropertyTypes.STRING,
			uiName = "Password",
			uiDescription = "The password to use when connecting to the database")
	private String password;
	public static final String CKEY_PASS = "password";

	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Connection pool min",
			uiDescription = "The minimum number of pooled connections in the underlying ConnectionPool")
	private int minPool;
	public static final String CKEY_MIN_POOL = "minPool";
	public static final int DEFAULT_MIN_POOL = 3;
	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Connection pool max",
			uiDescription = "The maximum number of pooled connections in the underlying ConnectionPool")
	private int maxPool;
	public static final String CKEY_MAX_POOL = "maxPool";
	public static final int DEFAULT_MAX_POOL = 30;
	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Connection pool max size",
			uiDescription = "The maximum number of connections in the underlying ConnectionPool")
	private int maxSize;
	public static final String CKEY_MAX_SIZE = "maxSize";
	public static final int DEFAULT_MAX_SIZE = 50;

	@SetupProperty(propertyType = SetupPropertyTypes.INT,
			uiName = "Idle timeout (seconds)",
			uiDescription = "The idle timeout (seconds) for connections in the underlying ConnectionPool")
	private int idleTimeoutS;
	public static final String CKEY_TIMEOUT = "idleTimeout";
	public static final int DEFAULT_IDLE_TIMEOUT_S = 120;

	public SqlConfig() {
		super();
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMinPool() {
		return minPool;
	}

	public void setMinPool(int minPool) {
		this.minPool = minPool;
	}

	public int getMaxPool() {
		return maxPool;
	}

	public void setMaxPool(int maxPool) {
		this.maxPool = maxPool;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getIdleTimeoutS() {
		return idleTimeoutS;
	}

	public void setIdleTimeoutS(int idleTimeoutS) {
		this.idleTimeoutS = idleTimeoutS;
	}

	@Override
	public String getUiName() {
		return "MySQL Configuration";
	}

	@Override
	public String getUiDescription() {
		return "Authorization information required to connect to a MySQL Database";
	}

	@Override
	public void resetConfiguration() {
		this.hostname = "";
		this.dbName = "";
		this.user = "";
		this.password = "";

		this.minPool = DEFAULT_MIN_POOL;
		this.maxPool = DEFAULT_MAX_POOL;
		this.maxSize = DEFAULT_MAX_SIZE;
		this.idleTimeoutS = DEFAULT_IDLE_TIMEOUT_S;
	}

	@Override
	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		this.hostname = config.getString(CKEY_HOSTNAME, this.hostname);
		this.dbName = config.getString(CKEY_DBNAME, this.dbName);
		this.user = config.getString(CKEY_USER, this.user);
		this.password = config.getString(CKEY_PASS, this.password);

		this.minPool = config.getInteger(CKEY_MIN_POOL, this.minPool);
		this.maxPool = config.getInteger(CKEY_MAX_POOL, this.maxPool);
		this.maxSize = config.getInteger(CKEY_MAX_SIZE, this.maxSize);
		this.idleTimeoutS = config.getInteger(CKEY_TIMEOUT, this.idleTimeoutS);
	}

	@Override
	protected void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		config.setProperty(CKEY_HOSTNAME, this.hostname);
		config.setProperty(CKEY_DBNAME, this.dbName);
		config.setProperty(CKEY_USER, this.user);
		config.setProperty(CKEY_PASS, this.password);

		config.setProperty(CKEY_MIN_POOL, this.minPool);
		config.setProperty(CKEY_MAX_POOL, this.maxPool);
		config.setProperty(CKEY_MAX_SIZE, this.maxSize);
		config.setProperty(CKEY_TIMEOUT, this.idleTimeoutS);
	}

}
