package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.props.LongProperty;

public abstract class QuerySourceConfig extends ComponentConfig implements
		QuerySourceInitializer {

	public QuerySourceConfig() {
		super();

		this.registerConfigProperty(rebuildIntervalMs);

		this.registerMediaProcessorName(QuerySource.PNAME_QADDED,
				QueryItem.class);
		this.registerMediaProcessorName(QuerySource.PNAME_QRMVED,
				QueryItem.class);
	}

	/*
	 * --------------------------------
	 * Config properties
	 * --------------------------------
	 */

	private static final String CKEY_REBUILD_INTERVAL_MS = "rebuildInterval";
	private static final long DEFAULT_REBUILD_INTERVAL_MS = 180 * 1000L;
	private static final String UINAME_REBUILD_INTERVAL_MS = "Query rebuild interval (ms)";
	private static final String UIDESC_REBUILD_INTERVAL_MS = "Interval (in milliseconds) of this QueryBuilder refreshing the queries from the data source";
	private final LongProperty rebuildIntervalMs = new LongProperty(
			CKEY_REBUILD_INTERVAL_MS, DEFAULT_REBUILD_INTERVAL_MS,
			UINAME_REBUILD_INTERVAL_MS, UIDESC_REBUILD_INTERVAL_MS);

	@Override
	public long getRebuildIntervalMs() {
		return this.rebuildIntervalMs.getVal();
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */
}
