package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.config.components.ComponentConfig;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.UserData;

public class UserToFollowQueryConfig extends ComponentConfig {

	public static final String TNAME = "User-to-query-follow";
	public static final String TDESC = "Produces Twitter follow-user queries corresponding to recieved UserData objects.";

	public UserToFollowQueryConfig() {
		super();

		this.registerMediaProcessorName("", UserData.class);
		this.registerMediaSourceName(UserToFollowQuery.SRCNAME_QUERIES,
				QueryFollow.class);
	}

	@Override
	protected Component instantiateComponent()
			throws MEaterConfigurationException {
		return new UserToFollowQuery(this);
	}

}
