package edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy;

import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QuerySourceInitializer;

public interface MySqlQuerySourceInitializer extends QuerySourceInitializer {
	public String getDbName();

	public String getQueryGroups();
}
