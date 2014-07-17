package edu.umd.rhsmith.diads.meater.modules.tweater.storage.legacy;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.config.props.StringProperty;

public class CsvStatusEaterConfig extends StatusEaterConfig implements
		CsvStatusEaterInitializer {

	public static final String TNAME = "CSVStatusEater-legacy";
	public static final String TDESC = "A (legacy) component which persists UserStatusData objects (information about both statuses and the users that posted them) to a local csv file.";

	public CsvStatusEaterConfig() {
		super();
		
		this.registerConfigProperty(filename);
	}

	@Override
	public Component instantiateComponent() throws IllegalStateException,
			MEaterConfigurationException {
		return new CsvStatusEater(this);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	private static final String CKEY_FILENAME = "filename";
	private static final String DEFAULT_FILENAME = "results.csv";
	private static final String UINAME_FILENAME = "output filename";
	private static final String UIDESC_FILENAME = "";
	private final StringProperty filename = new StringProperty(CKEY_FILENAME,
			DEFAULT_FILENAME, UINAME_FILENAME, UIDESC_FILENAME);

	@Override
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
