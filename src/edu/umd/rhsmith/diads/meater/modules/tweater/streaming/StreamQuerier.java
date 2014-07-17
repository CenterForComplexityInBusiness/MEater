package edu.umd.rhsmith.diads.meater.modules.tweater.streaming;

import java.util.Date;
import java.util.TreeSet;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaSource;
import edu.umd.rhsmith.diads.meater.core.app.components.media.sets.BaseMediaSetUpdater;
import edu.umd.rhsmith.diads.meater.core.app.components.media.sets.MediaSetUpdater;
import edu.umd.rhsmith.diads.meater.modules.tweater.TwitterManager;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.DefaultStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.DefaultUserData;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.DefaultUserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.StatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.UserData;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.UserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.oauth.OAuthInfo;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;
import edu.umd.rhsmith.diads.meater.util.Util;

/**
 * The main point of contact with the Twitter Streaming API.
 * 
 * This class periodically handles re-connecting to the Twitter Streaming API if
 * the query is out of date.
 * 
 * @author dmonner
 */
public class StreamQuerier extends Component implements Runnable,
		StatusListener {

	public static final String SRCNAME_TWEETS = "tweets";
	public static final String PNAME_QADD = "addQueries";
	public static final String PNAME_QRMV = "removeQueries";

	/**
	 * The connection to Twitter
	 */
	private TwitterStream tw;

	/**
	 * The minimum amount of time (ms) allowed between each request to Twitter
	 * for a change in the query
	 */
	private static final long MIN_UPDATE_INTERVAL_MS = 1 * 60 * 1000;

	/**
	 * Collection of active query items
	 */
	private TreeSet<QueryItem> queryItems;
	/**
	 * The time (in ms since the epoch) when the query was last sent to Twitter
	 */
	private long lastUpdate;
	/**
	 * Whether or not we need to send a new query to Twitter at the next
	 * opportunity
	 */
	private boolean needsUpdate;

	/**
	 * Whether or not we are shut down (permanently disconnected for this
	 * session)
	 */
	private boolean shutdownQuerierThread;

	/**
	 * <code>Thread</code> handling the rebuilding of queries
	 */
	private final Thread querierThread;

	private final MediaSource<UserStatusData> statusSource;
	private final MediaSetUpdater<QueryItem> queryUpdater;
	private final String oAuthName;

	public StreamQuerier(StreamQuerierInitializer init)
			throws MEaterConfigurationException {
		super(init);

		this.lastUpdate = Long.MIN_VALUE;
		this.needsUpdate = true;
		this.shutdownQuerierThread = false;
		this.querierThread = new Thread(this);

		this.statusSource = new MediaSource<UserStatusData>(SRCNAME_TWEETS,
				UserStatusData.class);
		this.registerMediaSource(this.statusSource);

		this.queryItems = new TreeSet<QueryItem>();
		this.queryUpdater = new BaseMediaSetUpdater<QueryItem>(PNAME_QADD,
				PNAME_QRMV, QueryItem.class) {
			@Override
			public boolean add(QueryItem item) {
				addItem(item);
				return true;
			}

			@Override
			public boolean remove(QueryItem item) {
				delItem(item);
				return true;
			}
		};
		this.registerMediaProcessor(this.queryUpdater.getMediaAdder());
		this.registerMediaProcessor(this.queryUpdater.getMediaRemover());
		this.oAuthName = init.getoAuthConfigurationName();
	}

	/**
	 * Adds a <code>QueryItem</code> to this querier
	 * 
	 * @param item
	 */
	protected void addItem(final QueryItem item) {
		synchronized (queryItems) {
			this.queryItems.add(item);
			this.needsUpdate = true;
		}
	}

	/**
	 * Removes a specific <code>QueryItem</code> from this querier
	 * 
	 * @param item
	 */
	protected void delItem(final QueryItem item) {
		synchronized (queryItems) {
			this.queryItems.add(item);
			this.needsUpdate = true;
		}
	}

	/*
	 * --------------------------------
	 * Status handling
	 * --------------------------------
	 */

	/**
	 * Connects to the Twitter Streaming API with the current query. If this
	 * querier has been marked as shut down, this method does nothing.
	 */
	protected void connect() {
		if (!shutdownQuerierThread && queryItems.size() > 0) {
			FilterQueryBuilder b;
			synchronized (this.queryItems) {
				b = new FilterQueryBuilder(this.queryItems);
			}
			logInfo(MSG_CONNECTING_FMT, b.toString());
			tw.filter(b.getFilterQuery());
		}
	}

	/**
	 * Disconnects from the Twitter Streaming API.
	 */
	protected void disconnect() {
		tw.cleanUp();
	}

	@Override
	public void onException(Exception ex) {
		// FIXME
		if (!ex.getMessage().contains("Stream closed")) {
			logSevere(Util.traceMessage(ex));
		}
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO: Implement something to do on StallWarnings
		// See also:
		// https://dev.twitter.com/docs/streaming-apis/parameters#stall_warnings
	}

	@Override
	public void onStatus(Status arg0) {
		StatusData status = new DefaultStatusData(arg0);
		UserData user = new DefaultUserData(arg0.getUser());
		UserStatusData us = new DefaultUserStatusData(user, status);

		this.statusSource.sourceMedia(us);
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	/**
	 * Shuts down this querier, disconnecting it permanently from Twitter.
	 */
	public void shutdown() {
		shutdownQuerierThread = true;
	}

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
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

		this.tw = new TwitterStreamFactory().getInstance();
		tw.setOAuthConsumer(oAuth.getConsumerKey(), oAuth.getConsumerSecret());
		tw.setOAuthAccessToken(new AccessToken(oAuth.getAccessToken(), oAuth
				.getAccessTokenSecret()));
	}

	@Override
	protected void doStartupRoutine() {
		// start the builder thread
		this.querierThread.start();

		// start listening on twitter
		tw.addListener(this);
	}

	@Override
	protected void doShutdownRoutine() {
		// disconnect from stream
		disconnect();

		// tell the thread to stop, and then wait for it to do so
		shutdownQuerierThread = true;
		try {
			this.querierThread.join();
		} catch (InterruptedException e) {
			logSevere(MSG_ERR_INTERRUPTED);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// Loop until we are shut down, periodically asking the QueryBuilder if
		// the query has been updated
		while (!shutdownQuerierThread) {
			long now = new Date().getTime();
			this.logFinest(MSG_RUNNING);
			this.logFinest(MSG_RUNNING_QUERIES_FMT, queryItems);

			// if we need to update, and it's been long enough since the
			// last update
			synchronized (queryItems) {
				if (needsUpdate && now > lastUpdate + MIN_UPDATE_INTERVAL_MS) {
					disconnect();
					connect();
					lastUpdate = now;
					needsUpdate = false;
				}
			}

			// Wait a while before starting the loop again
			try {
				Thread.sleep((int) (2000.0 + Math.random() * 1000.0));
			} catch (final InterruptedException ex) {
				logSevere(Util.traceMessage(ex));
			}
		}

		tw.shutdown();
		disconnect();

		this.logInfo(MSG_QUERYTHREAD_ENDED);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_AUTH_FMT = "Unable to load oAuth configuration name '%s'";
	private static final String MSG_ERR_NOTWMGR = "No twitter manager available for getting OAuth";
	private static final String MSG_RUNNING_QUERIES_FMT = "active query: %s";
	private static final String MSG_RUNNING = "Querier.run():";
	private static final String MSG_QUERYTHREAD_ENDED = "Querier shut down.";
	private static final String MSG_CONNECTING_FMT = "Querier connecting: %s";
	private static final String MSG_ERR_INTERRUPTED = "Interrupted during shutdown while awaiting querier-thread termination";
}
