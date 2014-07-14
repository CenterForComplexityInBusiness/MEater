package edu.umd.rhsmith.diads.meater.core.app.sql;

public interface SqlInfoSource {
	public SqlInfo getSqlInfo(String name) throws SqlLoadException;
}
