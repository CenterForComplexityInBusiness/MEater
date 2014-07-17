package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentInitializer;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaSource;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.UserData;
import edu.umd.rhsmith.diads.meater.util.ControlException;

public class UserToFollowQuery extends Component implements
		MediaProcessor<UserData> {

	public static final String SRCNAME_QUERIES = "queries";
	public static final String PNAME_USERS = "users";

	private final MediaSource<QueryFollow> querySource;

	public UserToFollowQuery(ComponentInitializer init)
			throws MEaterConfigurationException {
		super(init);

		this.querySource = new MediaSource<QueryFollow>(SRCNAME_QUERIES,
				QueryFollow.class);

		this.registerMediaProcessor(this);
		this.registerMediaSource(this.querySource);
	}

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
	}

	@Override
	protected void doStartupRoutine() throws ControlException {
	}

	@Override
	protected void doShutdownRoutine() {
	}

	@Override
	public Class<UserData> getMediaClass() {
		return UserData.class;
	}

	@Override
	public String getProcessorName() {
		return "";
	}

	@Override
	public boolean processMedia(UserData media) {
		this.querySource.sourceMedia(new QueryFollow(media.getUserId(), media
				.getUserId()));
		return true;
	}
}
