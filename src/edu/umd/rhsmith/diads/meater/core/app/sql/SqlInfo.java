package edu.umd.rhsmith.diads.meater.core.app.sql;

public interface SqlInfo {
	public String getHostname();

	public String getDbName();

	public String getUser();

	public String getPassword();

	public int getMinPool();

	public int getMaxPool();

	public int getMaxSize();

	public int getIdleTimeoutS();
}
