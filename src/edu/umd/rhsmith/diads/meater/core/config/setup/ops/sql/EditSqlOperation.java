package edu.umd.rhsmith.diads.meater.core.config.setup.ops.sql;

import edu.umd.rhsmith.diads.meater.core.app.sql.SqlConfiguration;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.EditExternalUnitOperation;

public class EditSqlOperation extends
		EditExternalUnitOperation<SqlConfiguration> {

	public EditSqlOperation() {
		super("Create or edit MySQL database credentials", "setup-mysql");
	}

	@Override
	protected SqlConfiguration createUnit() {
		SqlConfiguration o = new SqlConfiguration();
		o.resetConfiguration();
		return o;
	}

}
