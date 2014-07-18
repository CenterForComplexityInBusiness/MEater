package edu.umd.rhsmith.diads.meater.core.app.components.media.sets;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public abstract class MediaMatchFilterer<M, F extends MediaMatcher<M>> {

	private static final String PNAME_RMV_MATCHERS = "removeMatchers";
	private static final String PNAME_ADD_MATCHERS = "addMatchers";
	private static final String PNAME_ALL_NOT_MATCH = "allNotMatching";
	private static final String PNAME_ANY_MATCH = "anyMatching";
	private static final String PNAME_ANY_NOT_MATCH = "anyNotMatching";
	private static final String PNAME_ALL_MATCH = "allMatching";

	private final Set<F> queries;
	private MediaSetFilter<M> all;
	private MediaSetFilter<M> any;
	private MediaSetUpdater<F> matchUpdater;

	public MediaMatchFilterer() {
		this(null);
	}

	public MediaMatchFilterer(Comparator<F> matcherComparator) {
		this.queries = new TreeSet<F>(matcherComparator);
		this.all = null;
		this.any = null;
		this.matchUpdater = null;
	}

	/*
	 * --------------------------------
	 * Lazy-initialization helpers + override-able methods for subclasses
	 * --------------------------------
	 */

	public abstract Class<F> getMatcherClass();

	public String getMatcherAdderName() {
		return PNAME_ADD_MATCHERS;
	}

	public abstract Class<M> getMediaClass();

	public String getMatcherRemoverName() {
		return PNAME_RMV_MATCHERS;
	}

	public String getAllMatchName() {
		return PNAME_ALL_MATCH;
	}

	public String getAnyMatchName() {
		return PNAME_ANY_MATCH;
	}

	public String getAllNotMatchName() {
		return PNAME_ALL_NOT_MATCH;
	}

	public String getAnyNotMatchName() {
		return PNAME_ANY_NOT_MATCH;
	}

	private void initMatchUpdater() {
		this.matchUpdater = new SimpleMediaSetUpdater<F>(getMatcherAdderName(),
				getMatcherRemoverName(), getMatcherClass()) {
			@Override
			public boolean add(F media) {
				addMatcher(media);
				return true;
			}

			@Override
			public boolean remove(F media) {
				removeMatcher(media);
				return true;
			}
		};
	}

	private void initAnyFilter() {
		this.any = new SimpleMediaSetFilter<M>(getAnyMatchName(),
				getAllNotMatchName(), getMediaClass()) {
			@Override
			public boolean contains(M media) {
				return anyMatch(media);
			}
		};
	}

	private void initAllFilter() {
		this.all = new SimpleMediaSetFilter<M>(getAllMatchName(),
				getAnyNotMatchName(), getMediaClass()) {
			@Override
			public boolean contains(M media) {
				return allMatch(media);
			}
		};
	}

	/*
	 * --------------------------------
	 * Media entities
	 * --------------------------------
	 */

	public MediaSetFilter<M> getAnyFilter() {
		if (this.any == null) {
			this.initAnyFilter();
		}

		return this.any;
	}

	public MediaSetFilter<M> getAllFilter() {
		if (this.all == null) {
			this.initAllFilter();
		}

		return this.all;
	}

	public MediaSetUpdater<F> getMatcherUpdater() {
		if (this.matchUpdater == null) {
			this.initMatchUpdater();
		}

		return this.matchUpdater;
	}

	/*
	 * --------------------------------
	 * Programmatic media interaction
	 * --------------------------------
	 */

	public void removeMatcher(F media) {
		synchronized (this.queries) {
			this.queries.remove(media);
		}
	}

	public void addMatcher(F media) {
		synchronized (this.queries) {
			this.queries.add(media);
		}
	}

	public boolean allMatch(M media) {
		synchronized (this.queries) {
			for (MediaMatcher<M> matcher : this.queries) {
				if (!matcher.matches(media)) {
					return false;
				}
			}
			return true;
		}
	}

	public boolean anyMatch(M media) {
		synchronized (this.queries) {
			for (MediaMatcher<M> matcher : this.queries) {
				if (!matcher.matches(media)) {
					return false;
				}
			}
			return true;
		}
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */
}
