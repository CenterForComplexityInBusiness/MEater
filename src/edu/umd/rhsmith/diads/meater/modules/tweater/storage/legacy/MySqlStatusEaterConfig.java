package edu.umd.rhsmith.diads.meater.modules.tweater.storage.legacy;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.config.props.StringProperty;

public class MySqlStatusEaterConfig extends StatusEaterConfig implements
		MySqlStatusEaterInitializer {

	public static final String TNAME = "MySqlStatusEater-legacy";
	public static final String TDESC = "A (legacy) component which persists statuses and users, along with a wide variety of additional data, to a MySQL database."
			+ "\nNote that this component type requires a legacy TwEater database to function (see TwEater module help).";

	public MySqlStatusEaterConfig() {
		super();
		this.registerConfigProperty(dbName);
	}

	@Override
	public Component instantiateComponent() throws MEaterConfigurationException {
		return new MySqlStatusEater(this);
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

	@Override
	public String getDbName() {
		return this.dbName.getVal();
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
