package edu.umd.rhsmith.diads.meater.core.app.sql;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import snaq.db.DBPoolDataSource;
import edu.umd.rhsmith.diads.meater.core.app.MEaterInitializer;
import edu.umd.rhsmith.diads.meater.core.app.MEaterMain;
import edu.umd.rhsmith.diads.meater.core.config.sql.XmlSqlInfoSource;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.meater.util.ControlUnit;
import edu.umd.rhsmith.diads.meater.util.Util;

public class SqlManager extends ControlUnit {

	public static final String DB_DRIVER_CLASS = "com.mysql.jdbc.Driver";

	private final MEaterMain main;
	private final Map<String, DBPoolDataSource> dataSources;
	private SqlInfoSource sqlInfoSource;

	public SqlManager(MEaterInitializer init, MEaterMain main)
			throws SqlManagerCreationException {
		this.main = main;
		this.dataSources = new HashMap<String, DBPoolDataSource>();
		this.setLogger(main.getLogger());
		this.setLogName(main.getLogName());

		this.sqlInfoSource = new XmlSqlInfoSource();

		try {
			DriverManager.registerDriver((Driver) Class
					.forName(DB_DRIVER_CLASS).newInstance());
		} catch (InstantiationException e) {
			throw new SqlManagerCreationException(e);
		} catch (IllegalAccessException e) {
			throw new SqlManagerCreationException(e);
		} catch (ClassNotFoundException e) {
			throw new SqlManagerCreationException(e);
		} catch (SQLException e) {
			throw new SqlManagerCreationException(e);
		}
	}

	public MEaterMain getMain() {
		return main;
	}

	public SqlInfoSource getSqlInfoSource() {
		return sqlInfoSource;
	}

	public void setSqlInfoSource(SqlInfoSource sqlInfoSource) {
		this.sqlInfoSource = sqlInfoSource;
	}

	@Override
	protected void doStartupRoutine() throws ControlException {

	}

	@Override
	protected void doShutdownRoutine() {
		for (DBPoolDataSource d : this.dataSources.values()) {
			d.release();
		}
	}

	public DataSource getDataSource(String name) throws IllegalStateException {
		this.requireUnStopped();

		DBPoolDataSource source = this.dataSources.get(name);
		if (source == null) {
			source = this.createDataSource(name);
			if (source == null) {
				return null;
			}
			this.dataSources.put(name, source);
		}
		return source;
	}

	private DBPoolDataSource createDataSource(String name) {
		SqlInfo sql;
		
		if (this.sqlInfoSource == null) {
			this.logSevere(MSG_ERR_NO_SRC);
			return null;
		}
		try {
			sql = this.sqlInfoSource.getSqlInfo(name);
		} catch (SqlLoadException e) {
			this.logSevere(MSG_ERR_SRC_FAILED_FMT, name, Util.traceMessage(e));
			return null;
		}

		DBPoolDataSource ds = new DBPoolDataSource();
		ds.setDriverClassName(DB_DRIVER_CLASS);
		ds.setUrl("jdbc:" + "sql" + "://" + sql.getHostname() + "/"
				+ sql.getDbName() + "?continueBatchOnError=false"
				+ "&useUnicode=true" + "&characterEncoding=utf8"
				+ "&characterSetResults=utf8");
		ds.setUser(sql.getUser());
		ds.setPassword(sql.getPassword());
		ds.setMinPool(sql.getMinPool());
		ds.setMaxPool(sql.getMaxPool());
		ds.setMaxSize(sql.getMaxSize());
		ds.setIdleTimeout(sql.getIdleTimeoutS());
		ds.setValidatorClassName("snaq.db.AutoCommitValidator");

		return ds;
	}

	private static final String MSG_ERR_SRC_FAILED_FMT = "Sql auth source unable to retrieve authorization %s: %s";
	private static final String MSG_ERR_NO_SRC = "Sql auth information requested with no available source!";
}
