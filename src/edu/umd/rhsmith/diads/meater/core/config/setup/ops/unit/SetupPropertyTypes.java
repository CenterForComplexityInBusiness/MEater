package edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit;

public enum SetupPropertyTypes {
	BOOLEAN("boolean"), BYTE("byte"), CHAR("character"), SHORT("short"), INT(
			"int"), LONG("long"), FLOAT("float"), DOUBLE("double"), STRING(
			"string");

	private String uiName;

	private SetupPropertyTypes(String uiName) {
		this.uiName = uiName;
	}

	public String getUiNameName() {
		return this.uiName;
	}
}
