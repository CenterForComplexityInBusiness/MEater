package edu.umd.rhsmith.diads.tools.twitter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This program will get the Twitter timelines of the accounts
 * passed in.
 * 
 * Please note that the Twitter API returns only up to 3,200
 * of a user's most recent tweets.
 * 
 * Created On: October 21, 2013
 * 
 * @Author Praneet Puppala
 */
public class TimelineStream implements Runnable {

	private final Twitter twitter;
	private final LinkedBlockingQueue<Long> userIds;
	private boolean shutdownWhenEmpty;

	private final Set<TimelineStreamListener> listeners;

	private boolean shouldShutdown;

	public TimelineStream(String consumerKey, String consumerSecret,
			String accessToken, String accessTokenSecret) {
		this.shouldShutdown = false;
		this.shutdownWhenEmpty = false;

		this.userIds = new LinkedBlockingQueue<Long>();
		this.listeners = new HashSet<TimelineStreamListener>();

		// get oauth -> build twitter collection object
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(consumerKey);
		cb.setOAuthConsumerSecret(consumerSecret);
		cb.setOAuthAccessToken(accessToken);
		cb.setOAuthAccessTokenSecret(accessTokenSecret);

		// (timeout from original TLC code)
		cb.setHttpConnectionTimeout(40000);
		this.twitter = new TwitterFactory(cb.build()).getInstance();
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	@Override
	public void run() {
		// keep collecting until we're told to shut down, or run out of users
		while (!shouldShutdown) {
			this.collectNextTimeline();
			if (shutdownWhenEmpty && this.isUserQueueEmpty()) {
				this.setShouldShutdown(true);
			}
		}

		// notify listeners that we're done
		this.onShutdown();
	}

	public boolean isShutdownWhenEmpty() {
		return shutdownWhenEmpty;
	}

	public void setShutdownWhenEmpty(boolean shutdownWhenEmpty) {
		this.shutdownWhenEmpty = shutdownWhenEmpty;
	}

	public boolean isShouldShutdown() {
		return shouldShutdown;
	}

	public void setShouldShutdown(boolean shouldShutdown) {
		this.shouldShutdown = shouldShutdown;
	}

	/*
	 * --------------------------------
	 * Queries
	 * --------------------------------
	 */

	public void addUser(long userId) {
		this.userIds.add(userId);
	}

	public void addUsers(Collection<Long> userIds) {
		this.userIds.addAll(userIds);
	}

	public int getUserQueueSize() {
		return this.userIds.size();
	}

	public boolean isUserQueueEmpty() {
		return this.userIds.isEmpty();
	}

	protected long pollUserId() {
		Long userId = null;
		while (userId == null && !shouldShutdown) {
			try {
				userId = this.userIds.poll(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// stop trying if we're interrupted
				break;
			}
		}

		// if the queue gave us nothing, nothing to do
		if (userId == null) {
			return -1;
		}

		return userId;
	}

	/*
	 * --------------------------------
	 * Listener interaction
	 * --------------------------------
	 */

	public void addListener(TimelineStreamListener l) {
		synchronized (this.listeners) {
			this.listeners.add(l);
		}
	}

	public void removeListener(TimelineStreamListener l) {
		synchronized (this.listeners) {
			this.listeners.remove(l);
		}
	}

	protected void onUserStarted(long userId) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onUserStarted(userId);
			}
		}
	}

	protected void onUserPageStarted(long userId, int page) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onUserPageStarted(userId, page);
			}
		}
	}

	protected void onRateLimit(long userId, int page,
			RateLimitStatus rateLimitStatus) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onRateLimit(userId, page, rateLimitStatus);
			}
		}
	}

	protected void onException(long userId, int page, TwitterException ex) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onException(userId, page, ex);
			}
		}
	}

	protected void onStatus(Status status) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onStatus(status);
			}
		}
	}

	protected void onShutdown() {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onShutdown();
			}
		}
	}

	/*
	 * --------------------------------
	 * Timeline interaction
	 * --------------------------------
	 */

	// For each provided account, output all tweets available on timeline
	protected void collectNextTimeline() {
		// get the next user-query
		long userId = pollUserId();

		// -1 -> nothing to collect
		if (userId == -1) {
			return;
		}

		// okay, let us begin
		this.onUserStarted(userId);

		// get the initial timeline page
		int page = 1;
		List<Status> tweets = getUserTimelinePage(page, userId);

		// go through all tweets in the timeline page
		int currTweetIdx = 0;
		while (tweets != null && currTweetIdx < tweets.size()) {
			// output status
			this.onStatus(tweets.get(currTweetIdx));

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
	protected List<Status> getUserTimelinePage(int page, long userId) {
		List<Status> tweets = null;
		boolean successful = false;

		// alert listeners
		this.onUserPageStarted(userId, page);

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
				// cancel the collection if we don't know what to do
				if (!handlePageException(page, userId, e)) {
					return null;
				}
			}
			// also, don't keep trying if a shutdown has been requested.
		} while (!successful && !shouldShutdown);

		return tweets;
	}

	protected boolean handlePageException(int page, long userId,
			TwitterException e) {
		if (e.exceededRateLimitation()) {
			RateLimitStatus rls = e.getRateLimitStatus();
			return this.handleRateLimit(page, userId, rls);
		} else {
			// TODO find out the error conditions here and what we can /
			// can't recover from...
			// we don't know what to do. yet.
			this.onException(userId, page, e);
			return false;
		}
	}

	protected boolean handleRateLimit(int page, long userId, RateLimitStatus rls) {
		// rate limits are expected; just go to sleep for the requested
		// amount of time

		this.onRateLimit(userId, page, rls);

		int waitSeconds = rls.getSecondsUntilReset();
		if (waitSeconds < 0) {
			waitSeconds = 10;
		}

		try {
			Thread.sleep(waitSeconds * 1000L);
		} catch (InterruptedException e1) {
			// someone wants us to stop
			return false;
		}
		
		return true;
	}
}
