package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import org.apache.commons.configuration.HierarchicalConfiguration;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertiesEligible;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupProperty;
import edu.umd.rhsmith.diads.meater.core.config.setup.ops.unit.SetupPropertyTypes;

@SetupPropertiesEligible
public abstract class QuerySourceConfig extends ComponentConfig implements
		QuerySourceInitializer {

	private static final String CKEY_REBUILD_INTERVAL_MS = "rebuildInterval";
	private static final long DEFAULT_REBUILD_INTERVAL_MS = 180 * 1000L;

	public QuerySourceConfig() {
		super();

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

	@SetupProperty(propertyType = SetupPropertyTypes.LONG,
			uiName = "Query rebuild interval (ms)",
			uiDescription = "Interval (in milliseconds) of this QueryBuilder refreshing the queries from the data source")
	private long rebuildIntervalMs;

	public long getRebuildIntervalMs() {
		return this.rebuildIntervalMs;
	}

	/*
	 * --------------------------------
	 * Config operations
	 * --------------------------------
	 */

	@Override
	public void resetConfiguration() {
		this.rebuildIntervalMs = DEFAULT_REBUILD_INTERVAL_MS;
	}

	@Override
	protected void loadConfigurationPropertiesFrom(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.loadConfigurationPropertiesFrom(config);

		this.rebuildIntervalMs = config.getLong(CKEY_REBUILD_INTERVAL_MS,
				this.rebuildIntervalMs);
	}

	@Override
	protected void saveConfigurationPropertiesTo(
			HierarchicalConfiguration config)
			throws MEaterConfigurationException {
		super.saveConfigurationPropertiesTo(config);

		config.setProperty(CKEY_REBUILD_INTERVAL_MS, this.rebuildIntervalMs);
	}
}
