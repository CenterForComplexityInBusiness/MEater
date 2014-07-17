package edu.umd.rhsmith.diads.meater.modules.tweater.tlc;

// Import twitter API and other useful API's.
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
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

/**
 * This program will get the Twitter timelines of the accounts
 * passed in.
 * 
 * Please note that the Twitter API returns only up to 3,200
 * of a user's most recent tweets.
 * 
 * Requested accounts will be passed in via .txt (Text) files,
 * with each account being defined by the screen name, including
 * the '@' sign, and a new line for each account.
 * 
 * Created On: October 21, 2013
 * 
 * @Author Praneet Puppala
 */
public class TimelineCollector extends Component implements Runnable {

	public static final String SRCNAME_TWEETS = "tweets";
	public static final String PNAME_USERS = "users";

	private Twitter twitter;
	private final LinkedBlockingQueue<QueryFollow> userQueries;

	// TODO better means of record-keeping here? this will *eventually* fill up.
	// may want to switch to an external filter component when those systems are
	// ready, so it becomes the user's responsibility to keep track of collected
	// ids if they want to.
	private final HashSet<QueryFollow> collectedUsers;

	/**
	 * <code>Thread</code> handling the queries
	 */
	private final Thread querierThread;
	private boolean shutdownQuerierThread;

	private final MediaSource<UserStatusData> statusSource;
	private final MediaProcessor<QueryFollow> userProcessor;
	private final String oAuthName;

	public TimelineCollector(TimelineCollectorInitializer init)
			throws MEaterConfigurationException {
		super(init);

		this.shutdownQuerierThread = false;
		this.querierThread = new Thread(this);
		this.userQueries = new LinkedBlockingQueue<QueryFollow>();
		this.collectedUsers = new HashSet<QueryFollow>();

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

		this.oAuthName = init.getoAuthConfigurationName();
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
		// also get oauth -> build twitter collection object
		ConfigurationBuilder cb = new ConfigurationBuilder();

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

		cb.setOAuthConsumerKey(oAuth.getConsumerKey());
		cb.setOAuthConsumerSecret(oAuth.getConsumerSecret());
		cb.setOAuthAccessToken(oAuth.getAccessToken());
		cb.setOAuthAccessTokenSecret(oAuth.getAccessTokenSecret());
		// (timeout from original TLC code)
		cb.setHttpConnectionTimeout(40000);
		this.twitter = new TwitterFactory(cb.build()).getInstance();
	}

	@Override
	protected void doStartupRoutine() throws ControlException {
		this.querierThread.start();
	}

	@Override
	protected void doShutdownRoutine() {
		this.shutdownQuerierThread = true;
		try {
			this.querierThread.join();
		} catch (InterruptedException e) {
			this.logSevere(MSG_ERR_SHUTDOWN_INTERRUPTED);
		}
	}

	@Override
	public void run() {
		// just keep collecting until we're told to shut down
		while (!shutdownQuerierThread) {
			this.collectNextTimeline();
		}
		this.logInfo(MSG_QUERYTHREAD_ENDED);
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
		}
		this.userQueries.add(userQuery);
	}

	// For each provided account, output all tweets available on timeline
	private void collectNextTimeline() {
		// get the next user-query, unless we're shutting down
		QueryFollow query = null;
		while (query == null && !shutdownQuerierThread) {
			try {
				query = this.userQueries.poll(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// stop trying if we're interrupted
				break;
			}
		}
		// if the queue gave us nothing, nothing to do
		if (query == null) {
			return;
		}

		// record the user as collected so we don't collect them again
		synchronized (collectedUsers) {
			this.collectedUsers.add(query);
		}

		// get the id from the user query
		long userId = query.getUserId();
		logFine(MSG_COLLECTING_ID_FMT, userId);

		// get the initial timeline page
		int page = 1;
		List<Status> tweets = getUserTimelinePage(page, userId);

		// go through all tweets in the timeline page
		int currTweetIdx = 0;
		while (tweets != null && currTweetIdx < tweets.size()) {
			// output user-status object
			UserStatusData data = new DefaultUserStatusData(tweets
					.get(currTweetIdx));
			this.statusSource.sourceMedia(data);

			// if we're at the end of the page, reset and go to the next page
			if ((currTweetIdx % 199 == 0) && currTweetIdx != 0) {
				page++;
				tweets = getUserTimelinePage(page, userId);
				currTweetIdx = 0;
			} else {
				currTweetIdx++;
			}
		}
	}

	// get a given page of tweets
	private List<Status> getUserTimelinePage(int page, long userId) {
		List<Status> tweets = null;
		boolean successful = false;

		// we get nothing past page 16
		if (page > 16) {
			return null;
		}

		Paging paging = new Paging(page, 200);
		// keep trying to get the user's timeline unless something bad happens
		do {
			try {
				tweets = twitter.getUserTimeline(userId, paging);
				successful = true;
			} catch (TwitterException e) {
				// rate limits are expected; just go to sleep for the requested
				// amount of time
				if (e.exceededRateLimitation()) {
					int waitSeconds = e.getRateLimitStatus()
							.getSecondsUntilReset();
					logFine(MSG_RATELIMIT_FMT, page, waitSeconds);
					if (waitSeconds < 0) {
						waitSeconds = 10;
					}

					try {
						Thread.sleep(waitSeconds * 1000L);
					} catch (InterruptedException e1) {
						logSevere(MSG_ERR_LIMIT_INTERRUPTED);
					}

					successful = false;
				} else {
					// otherwise we don't know what to do. yet.
					// TODO find out the error conditions here and what we can /
					// can't recover from.
					logSevere(MSG_ERR_TWITEX_FMT, page, Util.traceMessage(e));
					return null;
				}
			}
			// also, don't keep trying if a shutdown has been requested.
		} while (!successful && !shutdownQuerierThread);

		return tweets;
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_AUTH_FMT = "Unable to load oAuth configuration name '%s'";
	private static final String MSG_ERR_NOTWMGR = "No twitter manager available for getting OAuth";
	private static final String MSG_ERR_SHUTDOWN_INTERRUPTED = "Interrupted while awaiting querier theread termination";
	private static final String MSG_QUERYTHREAD_ENDED = "Querier shut down.";
	private static final String MSG_COLLECTING_ID_FMT = "Collecting user id %ld";
	private static final String MSG_ERR_LIMIT_INTERRUPTED = "Interrupted while waiting for rate-limit expiration";
	private static final String MSG_ERR_TWITEX_FMT = "Twitter exception while getting timeline page %d: %s";
	private static final String MSG_RATELIMIT_FMT = "Rate-limited; waiting on page %d for %d seconds.";
}
