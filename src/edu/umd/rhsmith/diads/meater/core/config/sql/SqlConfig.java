package edu.umd.rhsmith.diads.meater.core.config.sql;

import edu.umd.rhsmith.diads.meater.core.app.sql.SqlInfo;
import edu.umd.rhsmith.diads.meater.core.config.ConfigUnit;
import edu.umd.rhsmith.diads.meater.core.config.props.IntProperty;
import edu.umd.rhsmith.diads.meater.core.config.props.StringProperty;

public class SqlConfig extends ConfigUnit implements SqlInfo {

	private static final String UINAME = "MySQL Configuration";
	private static final String UIDESC = "Authorization information required to connect to a MySQL Database";

	public static final String CKEY_HOSTNAME = "hostname";
	private static final String UINAME_HOSTNAME = "Host name";
	private static final String UIDESC_HOSTNAME = "The host name of the server to connect to";
	private final StringProperty hostname = new StringProperty(CKEY_HOSTNAME,
			"", UINAME_HOSTNAME, UIDESC_HOSTNAME);

	public static final String CKEY_DBNAME = "dbName";
	private static final String UINAME_DBNAME = "Database name";
	private static final String UIDESC_DBNAME = "The name of the database on the host machine to connect to";
	private final StringProperty dbName = new StringProperty(CKEY_DBNAME, "",
			UINAME_DBNAME, UIDESC_DBNAME);

	public static final String CKEY_USER = "user";
	private static final String UINAME_USER = "Username";
	private static final String UIDESC_USER = "The username to use when connecting to the database";
	private final StringProperty user = new StringProperty(CKEY_USER, "",
			UINAME_USER, UIDESC_USER);

	// FIXME passwords really, really should be done in a more secure way. i
	// think so, at least.
	public static final String CKEY_PASS = "password";
	private static final String UINAME_PASS = "Password";
	private static final String UIDESC_PASS = "The password to use when connecting to the database";
	private final StringProperty password = new StringProperty(CKEY_PASS, "",
			UINAME_PASS, UIDESC_PASS);

	public static final String CKEY_MIN_POOL = "minPool";
	public static final int DEFAULT_MIN_POOL = 3;
	private static final String UINAME_MIN_POOL = "Connection pool min";
	private static final String UIDESC_MIN_POOL = "The minimum number of pooled connections in the underlying ConnectionPool";
	private final IntProperty minPool = new IntProperty(CKEY_MIN_POOL,
			DEFAULT_MIN_POOL, UINAME_MIN_POOL, UIDESC_MIN_POOL);

	public static final String CKEY_MAX_POOL = "maxPool";
	public static final int DEFAULT_MAX_POOL = 30;
	private static final String UINAME_MAX_POOL = "Connection pool max";
	private static final String UIDESC_MAX_POOL = "The maximum number of pooled connections in the underlying ConnectionPool";
	private final IntProperty maxPool = new IntProperty(CKEY_MAX_POOL,
			DEFAULT_MAX_POOL, UINAME_MAX_POOL, UIDESC_MAX_POOL);

	public static final String CKEY_MAX_SIZE = "maxSize";
	public static final int DEFAULT_MAX_SIZE = 50;
	private static final String UINAME_MAX_SIZE = "Connection pool max size";
	private static final String UIDESC_MAX_SIZE = "The maximum number of connections in the underlying ConnectionPool";
	private final IntProperty maxSize = new IntProperty(CKEY_MAX_SIZE,
			DEFAULT_MAX_SIZE, UINAME_MAX_SIZE, UIDESC_MAX_SIZE);

	public static final String CKEY_TIMEOUT = "idleTimeout";
	public static final int DEFAULT_TIMEOUT = 120;
	private static final String UINAME_TIMEOUT = "Idle timeout (seconds)";
	private static final String UIDESC_TIMEOUT = "The idle timeout (seconds) for connections in the underlying ConnectionPool";
	private final IntProperty idleTimeoutS = new IntProperty(CKEY_TIMEOUT,
			DEFAULT_TIMEOUT, UINAME_TIMEOUT, UIDESC_TIMEOUT);

	public SqlConfig() {
		super();

		this.registerConfigProperty(hostname);
		this.registerConfigProperty(dbName);
		this.registerConfigProperty(user);
		this.registerConfigProperty(password);
		this.registerConfigProperty(minPool);
		this.registerConfigProperty(maxPool);
		this.registerConfigProperty(maxSize);
		this.registerConfigProperty(idleTimeoutS);
	}

	@Override
	public String getHostname() {
		return hostname.getVal();
	}

	public void setHostname(String hostname) {
		this.hostname.setVal(hostname);
	}

	@Override
	public String getDbName() {
		return dbName.getVal();
	}

	public void setDbName(String dbName) {
		this.dbName.setVal(dbName);
	}

	@Override
	public String getUser() {
		return user.getVal();
	}

	public void setUser(String user) {
		this.user.setVal(user);
	}

	@Override
	public String getPassword() {
		return password.getVal();
	}

	public void setPassword(String password) {
		this.password.setVal(password);
	}

	@Override
	public int getMinPool() {
		return minPool.getVal();
	}

	public void setMinPool(int minPool) {
		this.minPool.setVal(minPool);
	}

	@Override
	public int getMaxPool() {
		return maxPool.getVal();
	}

	public void setMaxPool(int maxPool) {
		this.maxPool.setVal(maxPool);
	}

	@Override
	public int getMaxSize() {
		return maxSize.getVal();
	}

	public void setMaxSize(int maxSize) {
		this.maxSize.setVal(maxSize);
	}

	@Override
	public int getIdleTimeoutS() {
		return idleTimeoutS.getVal();
	}

	public void setIdleTimeoutS(int idleTimeoutS) {
		this.idleTimeoutS.setVal(idleTimeoutS);
	}

	@Override
	public String getUiName() {
		return UINAME;
	}

	@Override
	public String getUiDescription() {
		return UIDESC;
	}
}
