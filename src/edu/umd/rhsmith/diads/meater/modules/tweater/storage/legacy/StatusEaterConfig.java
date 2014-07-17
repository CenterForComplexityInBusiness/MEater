package edu.umd.rhsmith.diads.meater.modules.tweater.storage.legacy;

import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.props.BooleanProperty;
import edu.umd.rhsmith.diads.meater.modules.tweater.UserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;

public abstract class StatusEaterConfig extends ComponentConfig implements
		StatusEaterInitializer {

	public StatusEaterConfig() {
		super();

		this.registerConfigProperty(discardsUnmatched);

		this.registerMediaProcessorName("", UserStatusData.class);
		this.registerMediaProcessorName(StatusEater.PNAME_QADD, QueryItem.class);
		this.registerMediaProcessorName(StatusEater.PNAME_QRMV, QueryItem.class);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	private static final String CKEY_DISCARDS = "discardsUnmatched";
	private static final boolean DEFAULT_DISCARDS = false;
	private static final String UINAME_DISCARDS = "Discard unmatched statuses";
	private static final String UIDESC_DISCARDS = "Whether or not to discard incloming statuses that do not match any recived QueryItems";
	private final BooleanProperty discardsUnmatched = new BooleanProperty(
			CKEY_DISCARDS, DEFAULT_DISCARDS, UINAME_DISCARDS, UIDESC_DISCARDS);

	@Override
	public boolean isDiscardsUnmatched() {
		return this.discardsUnmatched.getVal();
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

}
