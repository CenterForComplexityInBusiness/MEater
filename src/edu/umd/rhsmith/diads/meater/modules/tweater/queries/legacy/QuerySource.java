package edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.media.sets.BaseMediaSetUpdateViewer;
import edu.umd.rhsmith.diads.meater.core.app.components.media.sets.MediaSetUpdateViewer;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItemTime;
import edu.umd.rhsmith.diads.meater.util.Util;

/**
 * This class builds a set of query phrases that are currently active based on
 * an external data
 * source. The data source should specify what all query phrases to be
 * collected, as well as their
 * start and end times. This class runs the <code>update</code> method once per
 * hour in order to
 * re-sync with the data source, as that is where query changes should be made.
 * 
 * @author dmonner
 */
public abstract class QuerySource extends Component implements Runnable {

	/**
	 * The interval (ms) between updates from the data source
	 */
	protected final long buildIntervalMs;

	/**
	 * Parallel-indexed with <code>times</code>. The idea is that the
	 * <code>times</code> list contains
	 * all the times at which the query changes. The first index is when the
	 * first query begins, and
	 * the last index is when the last query ends. If the current time is
	 * between <code>times[i]</code> and <code>times[i+1]</code>, the current
	 * set of query phrases will be in <code>queries[i+1]</code>.
	 * <code>queries[0]</code> should always contain an empty set of
	 * queries, to be returned before/after the time bounds.
	 * 
	 * Both of these data structures should be built from the data source, from
	 * scratch, every time
	 * the <code>update</code> method is called.
	 */
	protected final ArrayList<TreeSet<QueryItem>> queries;
	protected final TreeSet<QueryItem> prevQueries;

	/**
	 * Parallel-indexed with <code>queries</code>; see that variable's
	 * description.
	 */
	protected final ArrayList<Long> times;

	/**
	 * If <code>true</code>, we should stop updating
	 */
	private boolean shutdownBuilderThread;
	/**
	 * <code>Thread</code> handling the rebuilding of queries
	 */
	private final Thread builderThread;

	private final MediaSetUpdateViewer<QueryItem> updater;

	public static final String PNAME_QADDED = "addedQueries";
	public static final String PNAME_QRMVED = "removedQueries";

	public QuerySource(QuerySourceInitializer init)
			throws MEaterConfigurationException {
		super(init);

		this.buildIntervalMs = init.getRebuildIntervalMs();
		this.times = new ArrayList<Long>();
		this.queries = new ArrayList<TreeSet<QueryItem>>();
		this.prevQueries = new TreeSet<QueryItem>();
		this.shutdownBuilderThread = false;

		this.updater = new BaseMediaSetUpdateViewer<QueryItem>(PNAME_QADDED, PNAME_QRMVED,
				QueryItem.class);
		this.registerMediaSource(this.updater.getAddedMedia());
		this.registerMediaSource(this.updater.getRemovedMedia());

		// building happens in its own thread
		this.builderThread = new Thread(this);
	}

	/*
	 * --------------------------------
	 * Query building
	 * --------------------------------
	 */

	/**
	 * Reads all query information from the data source and returns it, without
	 * regard to the
	 * information that the query builder currently knows; this will be computed
	 * by <code>set</code> separately.
	 * 
	 * @return A list of query items and associated times, fresh from the data
	 *         source.
	 */
	protected abstract List<QueryItemTime> getQueriesFromSource();

	/**
	 * Based on the information from the data source, constructs the query that
	 * would be active at the
	 * specified time.
	 * 
	 * @param time
	 * @return The query active at the given time
	 */
	public TreeSet<QueryItem> at(final long time) {
		synchronized (queries) {
			if (times.isEmpty())
				return new TreeSet<QueryItem>();

			// if the time is less than the first entry in the array, query is
			// empty (queries[0])
			if (time < times.get(0))
				return queries.get(0);

			// otherwise, search array for the appropriate slot by finding first
			// time greater
			for (int i = 1; i < times.size(); i++)
				if (time < times.get(i))
					return queries.get(i);

			// otherwise, time > biggest time, so query is empty (queries[0])
			return queries.get(0);
		}
	}

	/**
	 * Uses the most recent query information from the data source to
	 * intelligently update the query builder's timeline.
	 * 
	 * @param all
	 *            The most recent query information from the data source
	 */
	private void set(List<QueryItemTime> all) {
		synchronized (queries) {
			final TreeSet<Long> alltimes = new TreeSet<Long>();
			for (final QueryItemTime qpt : all) {
				alltimes.add(qpt.startTime);
				alltimes.add(qpt.endTime);
			}
			times.clear();
			times.addAll(alltimes);

			queries.clear();
			for (int i = 0; i < times.size(); i++)
				queries.add(new TreeSet<QueryItem>());

			for (final QueryItemTime qpt : all)
				for (int i = 0; i < times.size(); i++)
					if (qpt.startTime <= times.get(i)
							&& times.get(i) < qpt.endTime)
						queries.get(i + 1).add(qpt.item);
		}
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
	}

	@Override
	protected void doStartupRoutine() {
		// start the builder thread
		this.builderThread.start();
	}

	@Override
	protected void doShutdownRoutine() {
		// instruct the builder thread to stop, and then wait for it to do so
		shutdownBuilderThread = true;
		try {
			this.builderThread.join();
		} catch (InterruptedException e) {
			logSevere(MSG_ERR_SHUTDOWN_INTERRUPTED);
		}
	}

	// task executed by the builder thread
	@Override
	public void run() {
		// Main loop periodically updates the query from the data source
		while (!shutdownBuilderThread) {
			long now = new Date().getTime();
			this.logInfo(MSG_REBUILDING);

			// rebuild query listing from source
			List<QueryItemTime> all = getQueriesFromSource();
			if (all != null) {
				this.set(all);
			}

			// then send updates as queries become active, inactive
			refreshActiveQueries(now);

			// Wait a while before starting the loop again
			try {
				Thread.sleep(this.buildIntervalMs);
			} catch (InterruptedException ex) {
				logSevere(MSG_ERR_BUILDER_INTERRUPTED, Util.traceMessage(ex));
			}
		}

		logInfo(MSG_THREAD_ENDED);
	}

	private void refreshActiveQueries(long now) {
		Set<QueryItem> newQueries = this.at(now);

		// compare the "current" tree to the "active" tree
		final TreeSet<QueryItem> toAdd = new TreeSet<QueryItem>(newQueries);
		toAdd.removeAll(this.prevQueries);
		final TreeSet<QueryItem> toRemove = new TreeSet<QueryItem>(
				this.prevQueries);
		toRemove.removeAll(newQueries);

		// if there are differences
		if (!toAdd.isEmpty() || !toRemove.isEmpty()) {
			// send the change deltas to all servers
			for (QueryItem qi : toAdd) {
				addItem(qi);
			}

			for (QueryItem qi : toRemove) {
				delItem(qi);
			}

			this.prevQueries.clear();
			this.prevQueries.addAll(newQueries);
		}
	}

	private void delItem(QueryItem qi) {
		this.updater.getRemovedMedia().sourceMedia(qi);
	}

	private void addItem(QueryItem qi) {
		this.updater.getAddedMedia().sourceMedia(qi);
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_BUILDER_INTERRUPTED = "Interrupted while sleeping between rebuilds - %s";
	private static final String MSG_ERR_SHUTDOWN_INTERRUPTED = "Interrupted during shutdown while awaiting builder-thread termination";
	private static final String MSG_THREAD_ENDED = "Builder-thread shut down.";
	private static final String MSG_REBUILDING = "Rebuilding query set";
}
