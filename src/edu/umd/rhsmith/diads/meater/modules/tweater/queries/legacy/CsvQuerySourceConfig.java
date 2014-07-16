package edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.config.props.StringProperty;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QuerySourceConfig;

public class CsvQuerySourceConfig extends QuerySourceConfig implements
		CsvQuerySourceInitializer {

	public static final String TNAME = "CsvQuerySource-legacy";
	public static final String TDESC = "A (legacy) component which builds query-sets by periodically checking a local csv file.";

	public CsvQuerySourceConfig() {
		super();
	}

	@Override
	public Component instantiateComponent() throws MEaterConfigurationException {
		return new CsvQuerySource(this);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	private static final String CKEY_FILENAME = "filename";
	private static final String DEFAULT_FILENAME = "queries.csv";
	private static final String UINAME_FILENAME = "input filename";
	private static final String UIDESC_FILENAME = "";
	private final StringProperty filename = new StringProperty(CKEY_FILENAME,
			DEFAULT_FILENAME, UINAME_FILENAME, UIDESC_FILENAME);

	public String getFilename() {
		return this.filename.getVal();
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
