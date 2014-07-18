package edu.umd.rhsmith.diads.meater.modules.tweater.tlc;

import java.util.HashSet;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.TwitterException;
import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.media.BaseMediaProcessor;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaSource;
import edu.umd.rhsmith.diads.meater.modules.tweater.TwitterManager;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.DefaultUserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.UserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.oauth.OAuthInfo;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryFollow;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.meater.util.Util;
import edu.umd.rhsmith.diads.tools.twitter.TimelineStream;
import edu.umd.rhsmith.diads.tools.twitter.TimelineStreamListener;

public class TimelineCollector extends Component implements
		TimelineStreamListener {

	public static final String SRCNAME_TWEETS = "tweets";
	public static final String PNAME_USERS = "users";

	// TODO better means of record-keeping here? this will *eventually* fill up.
	// may want to switch to an external filter component when those systems are
	// ready, so it becomes the user's responsibility to keep track of collected
	// ids if they want to.
	private final HashSet<QueryFollow> collectedUsers;

	private Thread tlcThread;
	private TimelineStream tlc;

	private final MediaSource<UserStatusData> statusSource;
	private final MediaProcessor<QueryFollow> userProcessor;
	private final String oAuthName;

	public TimelineCollector(TimelineCollectorInitializer init)
			throws MEaterConfigurationException {
		super(init);

		this.collectedUsers = new HashSet<QueryFollow>();

		this.oAuthName = init.getoAuthConfigurationName();

		// media source for outputting statuses
		this.statusSource = new MediaSource<UserStatusData>(SRCNAME_TWEETS,
				UserStatusData.class);
		this.registerMediaSource(this.statusSource);

		// media processor for adding new queries
		this.userProcessor = new BaseMediaProcessor<QueryFollow>(PNAME_USERS,
				QueryFollow.class) {
			@Override
			public boolean processMedia(QueryFollow media) {
				addUser(media);
				return true;
			}
		};
		this.registerMediaProcessor(userProcessor);
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
		// get oauth -> build twitter collection object
		TwitterManager mgr = this.getComponentManager().getMain()
				.getRuntimeModule(TwitterManager.class);
		if (mgr == null) {
			throw new MEaterConfigurationException(MSG_ERR_NOTWMGR);
		}
		OAuthInfo oAuth = mgr.getOAuthInfo(oAuthName);
		if (oAuth == null) {
			throw new MEaterConfigurationException(this.messageString(
					MSG_ERR_AUTH_FMT, oAuthName));
		}
		this.tlc = new TimelineStream(oAuth.getConsumerKey(), oAuth
				.getConsumerSecret(), oAuth.getAccessToken(), oAuth
				.getAccessTokenSecret());
		this.tlc.setShutdownWhenEmpty(false);
		this.tlcThread = new Thread(this.tlc);
	}

	@Override
	protected void doStartupRoutine() throws ControlException {
		this.tlc.addListener(this);
		this.tlcThread.start();
	}

	@Override
	protected void doShutdownRoutine() {
		this.tlc.setShouldShutdown(true);
		try {
			this.tlcThread.join();
		} catch (InterruptedException e) {
			this.logSevere(MSG_ERR_SHUTDOWN_INTERRUPTED);
		}
	}

	/*
	 * --------------------------------
	 * Timeline interaction
	 * --------------------------------
	 */

	// add a user for timeline collection
	public void addUser(QueryFollow userQuery) {
		// skip incoming queries that have already been processed
		synchronized (this.collectedUsers) {
			if (this.collectedUsers.contains(userQuery)) {
				return;
			}
			this.collectedUsers.add(userQuery);
		}
		this.tlc.addUser(userQuery.getUserId());
	}

	@Override
	public void onUserStarted(long userId) {
		logFine(MSG_COLLECTING_ID_FMT, userId);
	}

	@Override
	public void onUserPageStarted(long userId, int page) {
		logFiner(MSG_COLLECTING_ID_PAGE_FMT, userId, page);
	}

	@Override
	public void onRateLimit(long userId, int page,
			RateLimitStatus rateLimitStatus) {
		logInfo(MSG_RATELIMIT_FMT, userId, page, rateLimitStatus
				.getSecondsUntilReset());
	}

	@Override
	public void onException(long userId, int page, TwitterException ex) {
		logWarning(MSG_ERR_TWITEX_FMT, userId, page, Util.traceMessage(ex));
	}

	@Override
	public void onStatus(Status status) {
		this.statusSource.sourceMedia(new DefaultUserStatusData(status));
	}

	@Override
	public void onShutdown() {
		this.logInfo(MSG_QUERYTHREAD_ENDED);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_AUTH_FMT = "Unable to load oAuth configuration name '%s'";
	private static final String MSG_ERR_NOTWMGR = "No twitter manager available for getting OAuth";
	private static final String MSG_ERR_SHUTDOWN_INTERRUPTED = "Interrupted while awaiting querier theread termination";

	private static final String MSG_QUERYTHREAD_ENDED = "Collector shut down.";
	private static final String MSG_COLLECTING_ID_FMT = "Collecting user id %d";
	private static final String MSG_COLLECTING_ID_PAGE_FMT = "Collecting user id %d page %d";
	private static final String MSG_ERR_TWITEX_FMT = "Twitter exception while getting user %d timeline page %d: %s";
	private static final String MSG_RATELIMIT_FMT = "Rate-limited; waiting on user %d page %d for %d seconds.";
}
