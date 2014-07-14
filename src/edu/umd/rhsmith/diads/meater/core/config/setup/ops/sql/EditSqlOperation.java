package edu.umd.rhsmith.diads.meater.core.config.setup.ops.sql;

import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.EditExternalUnitOperation;
import edu.umd.rhsmith.diads.meater.core.config.sql.SqlConfig;

public class EditSqlOperation extends
		EditExternalUnitOperation<SqlConfig> {

	public EditSqlOperation() {
		super("Create or edit MySQL database credentials", "setup-mysql");
	}

	@Override
	protected SqlConfig createUnit() {
		SqlConfig o = new SqlConfig();
		o.resetConfiguration();
		return o;
	}

}
