package edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.config.props.StringProperty;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QuerySourceConfig;

public class MySqlQuerySourceConfig extends QuerySourceConfig implements
		MySqlQuerySourceInitializer {

	public static final String TNAME = "MySqlQuerySource-legacy";
	public static final String TDESC = "A (legacy) component which builds query-sets by periodically querying a MySQL database.";

	public MySqlQuerySourceConfig() {
		super();

		this.registerConfigProperty(dbName);
		this.registerConfigProperty(queryGroups);
	}

	@Override
	public Component instantiateComponent() throws MEaterConfigurationException {
		return new MySqlQuerySource(this);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	private static final String CKEY_DBNAME = "dbName";
	private static final String DEFAULT_DBNAME = "";
	private static final String UINAME_DBNAME = "database name";
	private static final String UIDESC_DBNAME = "";
	private final StringProperty dbName = new StringProperty(CKEY_DBNAME,
			DEFAULT_DBNAME, UINAME_DBNAME, UIDESC_DBNAME);

	private static final String CKEY_QUERYGROUPS = "queryGroups";
	private static final String DEFAULT_QUERYGROUPS = "";
	private static final String UINAME_QUERYGROUPS = "query groups";
	private static final String UIDESC_QUERYGROUPS = "Subset of query group numbers to include in query; leave blank for all";
	private final StringProperty queryGroups = new StringProperty(
			CKEY_QUERYGROUPS, DEFAULT_QUERYGROUPS, UINAME_QUERYGROUPS,
			UIDESC_QUERYGROUPS);

	@Override
	public String getDbName() {
		return this.dbName.getVal();
	}

	@Override
	public String getQueryGroups() {
		return this.queryGroups.getVal();
	}

	/*
	 * --------------------------------
	 * UI
	 * --------------------------------
	 */

	@Override
	public String getUiDescription() {
		return TDESC;
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

}
