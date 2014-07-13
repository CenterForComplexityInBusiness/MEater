package edu.umd.rhsmith.diads.meater.core.app.sql;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import snaq.db.DBPoolDataSource;
import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.MEaterInitializer;
import edu.umd.rhsmith.diads.meater.core.app.MEaterMain;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.meater.util.ControlUnit;

public class SqlManager extends ControlUnit {

	public static final String DB_DRIVER_CLASS = "com.mysql.jdbc.Driver";

	private final MEaterMain main;
	private final Map<String, DBPoolDataSource> dataSources;

	public SqlManager(MEaterInitializer init, MEaterMain main)
			throws SqlManagerCreationException {
		this.main = main;
		this.dataSources = new HashMap<String, DBPoolDataSource>();
		this.setLogger(main.getLogger());
		this.setLogName(main.getLogName());

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

	@Override
	protected void doStartupRoutine() throws ControlException {

	}

	@Override
	protected void doShutdownRoutine() {
		for (DBPoolDataSource d : this.dataSources.values()) {
			d.release();
		}
	}

	public DataSource getDataSource(String name) throws IllegalStateException,
			NoSuchElementException {
		this.requireUnStopped();

		DBPoolDataSource source = this.dataSources.get(name);
		if (source == null) {
			source = this.createDataSource(name);

			if (source == null) {
				throw new NoSuchElementException();
			}

			this.dataSources.put(name, source);
		}

		return source;
	}

	private DBPoolDataSource createDataSource(String name) {
		SqlConfiguration sql = new SqlConfiguration();

		try {
			XMLConfiguration xml = new XMLConfiguration(name + ".xml");
			sql.loadConfigurationFrom(xml);
		} catch (ConfigurationException e) {
			this.logSevere(MSG_ERR_BAD_FILE_FMT, name, e.getMessage());
			return null;
		} catch (MEaterConfigurationException e) {
			this.logSevere(MSG_ERR_BAD_FILE_FMT, name, e.getMessage());
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

	private static final String MSG_ERR_BAD_FILE_FMT = "Malformed SQL configuration file (%s %s)";

}
