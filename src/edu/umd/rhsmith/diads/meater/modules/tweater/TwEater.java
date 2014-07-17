package edu.umd.rhsmith.diads.meater.modules.tweater;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.MEaterMain;
import edu.umd.rhsmith.diads.meater.core.config.ConfigModule;
import edu.umd.rhsmith.diads.meater.modules.tweater.oauth.EditOAuthOperation;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryFollow;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryLocation;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryPhrase;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryTrack;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy.CsvQuerySourceConfig;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy.MySqlQuerySourceConfig;
import edu.umd.rhsmith.diads.meater.modules.tweater.storage.legacy.CsvStatusEaterConfig;
import edu.umd.rhsmith.diads.meater.modules.tweater.storage.legacy.MySqlStatusEaterConfig;
import edu.umd.rhsmith.diads.meater.modules.tweater.streaming.StreamQuerierConfig;
import edu.umd.rhsmith.diads.meater.modules.tweater.tlc.TimelineCollectorConfig;

public class TwEater extends ConfigModule {

	public TwEater() {
		super("TwEater");

		this.registerSetupConsoleOperation(new EditOAuthOperation());
		this.registerSetupConsoleOperation(new SetupLegacyDbOperation());

		this.registerMediaType(StatusData.class);
		this.registerMediaType(UserStatusData.class);
		this.registerMediaType(UserData.class);
		this.registerMediaType(QueryItem.class);
		this.registerMediaType(QueryFollow.class);
		this.registerMediaType(QueryLocation.class);
		this.registerMediaType(QueryTrack.class);
		this.registerMediaType(QueryPhrase.class);

		this.registerConfigType(StreamQuerierConfig.class);
		this.registerConfigType(TimelineCollectorConfig.class);
		this.registerConfigType(CsvQuerySourceConfig.class);
		this.registerConfigType(MySqlQuerySourceConfig.class);
		this.registerConfigType(CsvStatusEaterConfig.class);
		this.registerConfigType(MySqlStatusEaterConfig.class);
	}

	@Override
	public void addTo(MEaterMain main) throws MEaterConfigurationException {
		super.addTo(main);
		main.addRuntimeModule(new TwitterManager());
	}

	@Override
	public String getUiName() {
		return "TwEater";
	}

	@Override
	public String getUiDescription() {
		return "A module providing Twitter-oriented functionality, largely ported from the original TwEater project";
	}

}
