package edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy;


public interface MySqlQuerySourceInitializer extends QuerySourceInitializer {
	public String getDbName();

	public String getQueryGroups();
}
