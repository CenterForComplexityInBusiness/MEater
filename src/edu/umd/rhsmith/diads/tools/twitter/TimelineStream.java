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
 * <p>
 * A collection tool which will get all accessible statuses from the Twitter
 * timelines of given users, passing them and other collection events to
 * registered {@link TimelineStreamListener} instances.
 * </p>
 * <p>
 * <strong>Please note that the Twitter API returns only up to 3,200 of a user's
 * most recent tweets.</strong>
 * </p>
 * <p>
 * Collection is executed via the {@link #run()} method. User collection is
 * queue-based (that is, first-in / first-out); users may be added to the
 * collection queue via {@link #addUser(long)} or {@link #addUsers(Collection)}.
 * The user queue is thread-safe, so multiple threads may add collections to the
 * queue at once without synchronization concerns. However, the collection
 * operation should only be performed with one thread. Calling {@link #run()}
 * from multiple threads simultaneously will result in undefined behavior.
 * <p>
 * <p>
 * Collection will run until the should-shut-down state (visible via
 * {@link #isShouldShutdown()}) evaluates to {@code true}. When the state is set
 * to {@code true}, an active collection will halt after processing the current
 * timeline page of the current user; termination is not guaranteed to be
 * instantaneous. This state may be directly set via
 * {@link #setShouldShutdown(boolean)}. This state may also be set to become
 * {@code true} automatically when the user-queue is determined to be empty via
 * {@link #setShutdownWhenEmpty(boolean)}. (This property is {@code false} by
 * default). Once shut down, the should-shut-down state of a
 * {@code TimelineStream} instance is reset, allowing instances to be re-used
 * for multiple collections.
 * </p>
 * <p>
 * {@code TimelineStreamListener} instances are registered and unregistered via
 * {@link #addListener(TimelineStreamListener)} and
 * {@link #removeListener(TimelineStreamListener)} respectively. These methods
 * are thread-safe.
 * </p>
 * <p>
 * Subclasses may override {@code #run()}, the collection methods, and
 * user-queue methods interaction methods to provide a custom collection method
 * or user-queue implementation. Subclasses may also override the internal
 * {@link Twitter} instance used for collection.
 * </p>
 * <p>
 * Created On: October 21, 2013
 * </p>
 * 
 * @author Praneet Puppala
 * @author rmachedo
 * 
 * @see TimelineStreamListener
 */
public class TimelineStream implements Runnable {

	private final Twitter twitter;

	private final LinkedBlockingQueue<Long> userIds;
	private boolean shutdownWhenEmpty;
	private boolean shouldShutdown;

	private final Set<TimelineStreamListener> listeners;

	/**
	 * Creates a new {@code TimelineStream} instance with the given OAuth
	 * information to use for collection.
	 * 
	 * @param consumerKey
	 *            the Twitter consumer key to use
	 * @param consumerSecret
	 *            the Twitter consumer secret to use
	 * @param accessToken
	 *            the Twitter access token to use
	 * @param accessTokenSecret
	 *            the Twitter access token secret to use
	 */
	public TimelineStream(String consumerKey, String consumerSecret,
			String accessToken, String accessTokenSecret) {
		this.setShouldShutdown(false);
		this.setShutdownWhenEmpty(false);

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

	/**
	 * Creates a new {@code TimelineStream} instance with the given
	 * {@link Twitter} instance to use for collection
	 * 
	 * @param twitter
	 *            the {@link Twitter} instance to use
	 */
	public TimelineStream(Twitter twitter) {
		this.setShouldShutdown(false);
		this.setShutdownWhenEmpty(false);

		this.userIds = new LinkedBlockingQueue<Long>();
		this.listeners = new HashSet<TimelineStreamListener>();

		this.twitter = twitter;
	}

	/**
	 * Creates a new {@code TimelineStream} instance with no {@link Twitter}
	 * instance to use for collection. This constructor is for use by subclasses
	 * which also override the {@link #getTwitter()} method to supply and manage
	 * {@code Twitter} instances. If that method is not overridden when this
	 * constructor is used, it will return {@code null} and a
	 * {@code NullPointerException} may be thrown by {@code run()} during
	 * collection.
	 */
	protected TimelineStream() {
		this(null);
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	/**
	 * <p>
	 * Begin collection of user timelines currently in this
	 * {@code TimelineStream} instance's user queue until a shutdown is
	 * requested or the queue is empty and {@link #isShutdownWhenEmpty()} is set
	 * to {@code true}. Events encountered during collection will be passed to
	 * registered TimelineStreamListener instances. When collection finishes,
	 * notifies listeners via {@link TimelineStreamListener#onShutdown()} and
	 * resets this {@code TimelineStream}'s should-shut-down state.
	 * </p>
	 */
	@Override
	public void run() {
		// keep collecting until we're told to shut down, or run out of users
		while (!isShouldShutdown()) {
			testEmptyShutdown();
			this.collectNextTimeline();
			testEmptyShutdown();
		}

		// notify listeners that we're done
		this.onShutdown();
		this.setShouldShutdown(false);
	}

	/**
	 * <p>
	 * Tests whether this <code>TimelineStream</code> instance should set its
	 * should-shut-down state to <code>true</code> if its user queue is empty.
	 * </p>
	 * 
	 * @return <code>true</code> if this <code>TimelineStream</code> instance
	 *         should set its
	 *         should-shut-down state to <code>true</code> if its user queue is
	 *         empty, or <code>false</code> if not.
	 * 
	 * @see #setShutdownWhenEmpty(boolean)
	 */
	public final boolean isShutdownWhenEmpty() {
		return shutdownWhenEmpty;
	}

	/**
	 * <p>
	 * Sets whether this <code>TimelineStream</code> instance should set its
	 * should-shut-down state to <code>true</code> if its user queue is empty.
	 * </p>
	 * 
	 * @param shutdownWhenEmpty
	 *            <code>true</code> if this <code>TimelineStream</code> instance
	 *            should set its
	 *            should-shut-down state to <code>true</code> if its user queue
	 *            is
	 *            empty, or <code>false</code> if not.
	 * 
	 * @see #isShutdownWhenEmpty()
	 */
	public final void setShutdownWhenEmpty(boolean shutdownWhenEmpty) {
		this.shutdownWhenEmpty = shutdownWhenEmpty;
	}

	/**
	 * <p>
	 * Tests whether this <code>TimelineStream</code> instance should shut down
	 * its collection.
	 * </p>
	 * 
	 * @return <code>true</code> if this <code>TimelineStream</code> instance
	 *         should shut down
	 *         its collection, or <code>false</code> if not.
	 * 
	 * @see #setShouldShutdown(boolean)
	 */
	public final boolean isShouldShutdown() {
		return shouldShutdown;
	}

	/**
	 * <p>
	 * Sets whether this <code>TimelineStream</code> instance should shut down
	 * its collection.
	 * </p>
	 * 
	 * @param shouldShutdown
	 *            <code>true</code> if this <code>TimelineStream</code> instance
	 *            should shut down
	 *            its collection, or <code>false</code> if not.
	 * 
	 * @see #isShouldShutdown()
	 */
	public final void setShouldShutdown(boolean shouldShutdown) {
		this.shouldShutdown = shouldShutdown;
	}

	/**
	 * An internal-use method that sets this {@code TimelineStream} instance's
	 * should-shut-down state to <code>true</code> if its user-queueis empty and
	 * {@link #isShutdownWhenEmpty()} returns <code>true</code>.
	 */
	private void testEmptyShutdown() {
		if (isShutdownWhenEmpty() && this.getUserQueueSize() <= 0) {
			this.setShouldShutdown(true);
		}
	}

	/*
	 * --------------------------------
	 * Queries
	 * --------------------------------
	 */

	/**
	 * <p>
	 * Add a given user id to this {@code TimelineStream} instance's user queue.
	 * </p>
	 * <p>
	 * This method is thread-safe.
	 * </p>
	 * 
	 * @param userId
	 *            the user id to collect
	 * 
	 * @see #addUsers(Collection)
	 */
	public void addUser(long userId) {
		this.userIds.add(userId);
	}

	/**
	 * <p>
	 * Add a given collection of user ids to this {@code TimelineStream}
	 * instance's user queue.
	 * </p>
	 * <p>
	 * This method is thread-safe.
	 * </p>
	 * 
	 * @param userIds
	 *            the user ids to collect
	 * 
	 * @see #addUser(long)
	 */
	public void addUsers(Collection<Long> userIds) {
		this.userIds.addAll(userIds);
	}

	/**
	 * <p>
	 * Gets the current size of this {@code TimelineStream} instance's user
	 * queue. If this method returns <code>0</code> and
	 * {@link #isShutdownWhenEmpty()} is set to <code>true</code>, collection
	 * will stop.
	 * </p>
	 * <p>
	 * This method is thread-safe.
	 * </p>
	 * 
	 * @return the current size of this {@code TimelineStream} instance's user
	 *         queue
	 */
	public int getUserQueueSize() {
		return this.userIds.size();
	}

	/**
	 * <p>
	 * Remove and return the next user id in this {@code TimelineStream}
	 * instance's user queue. Stops attempting to get an id and returns
	 * <code>-1</code> if shutdown is requested via {@link #isShouldShutdown()},
	 * or if the queue is empty and {@link #isShutdownWhenEmpty()} returns
	 * <code>true</code>.
	 * </p>
	 * 
	 * @return the next user id in this {@code TimelineStream} instance's user
	 *         queue
	 * 
	 * @see #addUser(long)
	 * @see #addUsers(Collection)
	 * @see #getUserQueueSize()
	 */
	protected long pollUserId() {
		Long userId = null;
		while (userId == null && !isShouldShutdown()) {
			try {
				userId = this.userIds.poll(10, TimeUnit.SECONDS);
				testEmptyShutdown();
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

	/**
	 * <p>
	 * Register a new {@link TimelineStreamListener} instance to this
	 * {@code TimelineStream}. Has no effect if the given instance has already
	 * been registered.
	 * </p>
	 * <p>
	 * This method is thread-safe.
	 * </p>
	 * 
	 * @param l
	 *            the {@link TimelineStreamListener} instance to register
	 * 
	 * @see TimelineStreamListener
	 * @see #removeListener(TimelineStreamListener)
	 */
	public final void addListener(TimelineStreamListener l) {
		synchronized (this.listeners) {
			this.listeners.add(l);
		}
	}

	/**
	 * <p>
	 * Unregister a new {@link TimelineStreamListener} instance from this
	 * {@code TimelineStream}. Has no effect if the given instance has not yet
	 * been registered.
	 * </p>
	 * <p>
	 * This method is thread-safe.
	 * </p>
	 * 
	 * @param l
	 *            the {@link TimelineStreamListener} instance to unregister
	 * 
	 * @see TimelineStreamListener
	 * @see #addListener(TimelineStreamListener)
	 */
	public final void removeListener(TimelineStreamListener l) {
		synchronized (this.listeners) {
			this.listeners.remove(l);
		}
	}

	/**
	 * <p>
	 * Notify registered {@code TimelineStreamListener} instances of a that the
	 * given user id is about to be collected.
	 * </p>
	 * 
	 * @param userId
	 *            the Twitter ID of the
	 *            to-be-collected user
	 * @see TimelineStreamListener#onUserStarted(long)
	 */
	protected final void onUserStarted(long userId) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onUserStarted(userId);
			}
		}
	}

	/**
	 * <p>
	 * Notify registered {@code TimelineStreamListener} instances of a that the
	 * given page index of the given user id is about to be collected.
	 * </p>
	 * 
	 * @param userId
	 *            the Twitter ID of the
	 *            to-be-collected user
	 * @param page
	 *            the timeline page number that is about to be
	 *            retrieved.
	 * @see TimelineStreamListener#onUserPageStarted(long, int)
	 */
	protected final void onUserPageStarted(long userId, int page) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onUserPageStarted(userId, page);
			}
		}
	}

	/**
	 * <p>
	 * Notify registered {@code TimelineStreamListener} instances of a
	 * {@link RateLimitStatus} encountered while collecting the given user's
	 * timeline.
	 * </p>
	 * 
	 * @param userId
	 *            the Twitter ID of the
	 *            user being collected
	 * @param page
	 *            the page index that is about to be
	 *            retrieved.
	 * @param rateLimitStatus
	 *            the {@link RateLimitStatus} corresponding to the
	 *            rate-limit.
	 * @see TimelineStreamListener#onRateLimit(long, int, RateLimitStatus)
	 */
	protected final void onRateLimit(long userId, int page,
			RateLimitStatus rateLimitStatus) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onRateLimit(userId, page, rateLimitStatus);
			}
		}
	}

	/**
	 * <p>
	 * Notify registered {@code TimelineStreamListener} instances of a
	 * {@link TwitterException} encountered while collecting the given user's
	 * timeline.
	 * </p>
	 * 
	 * @param userId
	 *            the Twitter ID of the
	 *            user being collected
	 * @param page
	 *            the page index that is about to be
	 *            retrieved.
	 * @param rateLimitStatus
	 *            the {@link RateLimitStatus} corresponding to the
	 *            rate-limit.
	 * @see TimelineStreamListener#onException(long, int, TwitterException)
	 */
	protected final void onException(long userId, int page, TwitterException ex) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onException(userId, page, ex);
			}
		}
	}

	/**
	 * <p>
	 * Notify registered {@code TimelineStreamListener} instances of a
	 * {@link Status} collected from the current user's timeline.
	 * </p>
	 * 
	 * @param status
	 *            the collected {@link Status} object
	 * @see TimelineStreamListener#onStatus(Status)
	 */
	protected final void onStatus(Status status) {
		synchronized (this.listeners) {
			for (TimelineStreamListener l : this.listeners) {
				l.onStatus(status);
			}
		}
	}

	/**
	 * <p>
	 * Notify registered {@code TimelineStreamListener} instances that this
	 * {@code TimelineStream} instance has shut down it current collection.
	 * </p>
	 * 
	 * @see TimelineStreamListener#onShutdown()
	 */
	protected final void onShutdown() {
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

	/**
	 * <p>
	 * Get the {@link Twitter} instance associated with this
	 * {@code TimelineStream}, which is used for collecting user pages.
	 * </p>
	 * 
	 * @return the {@link Twitter} instance associated with this
	 *         {@code TimelineStream}
	 */
	protected Twitter getTwitter() {
		return this.twitter;
	}

	/**
	 * <p>
	 * Collect the tweets on the timeline of the next user id in the user-queue
	 * as returned by {@link #pollUserId()}. If the poll method returns
	 * {@code -1}, no collection will be executed. Additionally, if
	 * {@link #getUserTimelinePage(int, long)} returns a {@code null} list while
	 * collecting timeline pages, collection will halt.
	 * </p>
	 * <p>
	 * Collected tweets are reported to registered listeners via
	 * {@link #onStatus(Status)}.
	 * </p>
	 */
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

		// keep going while we have a page
		while (tweets != null) {
			// go through all tweets in the timeline page
			for (Status tweet : tweets) {
				this.onStatus(tweet);
			}
			// try to move to the next page
			++page;
			tweets = getUserTimelinePage(page, userId);
		}
	}

	/**
	 * <p>
	 * Collect and return a list of {@link Status} objects corresponding to the
	 * given page index (for pages of 200 tweets) from the given user's
	 * timeline. If the page cannot be retrieved, return {@code null} to
	 * indicate that the current user's collection should be stopped.
	 * </p>
	 * <p>
	 * If an exception is raised while collecting the page, attempts to handle
	 * it via {@link #handlePageException(int, long, TwitterException)}.
	 * Continues attempting to collect if the method returns {@code true};
	 * otherwise returns {@code null} to halt the collection.
	 * </p>
	 * 
	 * @param page
	 *            the timeline page index to be collected
	 * @param userId
	 *            the id of the user to collect
	 * @return a list of {@link Status} objects corresponding to the
	 *         given page index, or {@code null} if the page cannot be
	 *         retrieved
	 */
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
				tweets = getTwitter().getUserTimeline(userId, paging);
				successful = true;
			} catch (TwitterException e) {
				// cancel the collection if we don't know what to do
				if (!handlePageException(page, userId, e)) {
					return null;
				}
			}
			// also, don't keep trying if a shutdown has been requested.
		} while (!successful && !isShouldShutdown());

		return tweets;
	}

	/**
	 * <p>
	 * Respond to an exception encountered during a timeline collection. Return
	 * {@code true} if the collection of the given user may continue, or
	 * {@code false} if the collection of the given user should terminate.
	 * <p>
	 * <p>
	 * This method checks if the exception represents a rate-limitation,
	 * invoking and returning the result of
	 * {@link #handleRateLimit(int, long, RateLimitStatus)} if so; otherwise
	 * notifies registered listeners via
	 * {@link #onException(long, int, TwitterException)} and returns
	 * {@code false}.
	 * </p>
	 * 
	 * @param page
	 *            the page number being collected when the rate-limit was
	 *            encountered.
	 * @param userId
	 *            the user being collected when the rate-limit was
	 *            encountered.
	 * @param e
	 *            the {@link TwitterException} that was encountered
	 * @return {@code true} if the collection of the given user may
	 *         continue, or {@code false} if the collection of the given
	 *         user should terminate.
	 */
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

	/**
	 * <p>
	 * Respond to a rate limitation encountered during a timeline collection.
	 * Return {@code true} if the collection of the given user may continue, or
	 * {@code false} if the collection of the given user should terminate.
	 * <p>
	 * <p>
	 * This method waits for the seconds-until-reset value in the given
	 * {@link RateLimitStatus} and then returns {@code true}; unless the
	 * executing thread is interrupted, in which case {@code false} is
	 * immediately returned. Before waiting, also notifies registered listeners
	 * via {@link #onRateLimit(long, int, RateLimitStatus)}.
	 * </p>
	 * 
	 * @param page
	 *            the page number being collected when the rate-limit was
	 *            encountered.
	 * @param userId
	 *            the user being collected when the rate-limit was
	 *            encountered.
	 * @param rls
	 *            the {@link RateLimitStatus} that was encountered
	 * @return {@code true} if the collection of the given user may
	 *         continue, or {@code false} if the collection of the given
	 *         user should terminate.
	 */
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
