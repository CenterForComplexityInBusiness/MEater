package edu.umd.rhsmith.diads.meater.modules.tweater.storage.legacy;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;
import edu.umd.rhsmith.diads.meater.core.app.components.media.sets.BaseMediaSetUpdater;
import edu.umd.rhsmith.diads.meater.core.app.components.media.sets.MediaSetUpdater;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.UserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;

/**
 * Base implementation of a <code>StatusEater</code> that maintains a set of
 * <code>QueryItem</code>s
 * that it cares about. It implements basic methods for adding to and deleting
 * from this set, and
 * implements <code>process</code> to find all <code>QueryItem</code>s
 * associated with a given <code>Status<code>. Leaves the <code>persist</code>
 * method for implementation by subclasses
 * focusing on a specific long-term storage system.
 * 
 * @author dmonner
 * 
 */
public abstract class StatusEater extends Component implements
		MediaProcessor<UserStatusData> {
	public static final String PNAME_QRMV = "removeQueries";
	public static final String PNAME_QADD = "addQueries";

	/**
	 * The set of <code>QueryItem</code>s that this <code>StatusEater</code>
	 * cares about.
	 */
	private final Set<QueryItem> queries;
	private final MediaSetUpdater<QueryItem> queryUpdater;

	private final boolean discardsUnmatched;

	public StatusEater(StatusEaterInitializer init)
			throws MEaterConfigurationException {
		super(init);

		this.discardsUnmatched = init.isDiscardsUnmatched();

		this.registerMediaProcessor(this);

		this.queries = new CopyOnWriteArraySet<QueryItem>();
		this.queryUpdater = new BaseMediaSetUpdater<QueryItem>(PNAME_QADD,
				PNAME_QRMV, QueryItem.class) {
			@Override
			public boolean add(QueryItem media) {
				queries.add(media);
				return true;
			}

			@Override
			public boolean remove(QueryItem media) {
				queries.remove(media);
				return true;
			}
		};
		this.registerMediaProcessor(queryUpdater.getMediaAdder());
		this.registerMediaProcessor(queryUpdater.getMediaRemover());
	}

	@Override
	public Class<UserStatusData> getMediaClass() {
		return UserStatusData.class;
	}

	@Override
	public String getProcessorName() {
		return "";
	}

	@Override
	public boolean processMedia(UserStatusData media) {
		this.eat(media);
		return true;
	}

	/**
	 * Instructs this <code>StatusEater</code> to match the given
	 * <code>Status</code> against its list
	 * of <code>QueryItem</code>s to see which match, and then pass the results
	 * along to <code>persist</code>.
	 * 
	 * @param status
	 */
	public void eat(UserStatusData status) {
		logFinest(MSG_PROCESSING_FMT, status.getStatusId());

		// generate a list of QueryItems that match the status
		List<QueryItem> matched = new LinkedList<QueryItem>();
		for (final QueryItem item : queries)
			if (item.matches(status))
				matched.add(item);

		// if there are any matches, or we don't factor matches into the choice
		// to persist, persist the status
		if (!matched.isEmpty() || !discardsUnmatched) {
			persist(matched, status);
		} else {
			logFine(MSG_SKIP_UNMATCHED_FMT, status.getStatusId());
		}
	}

	/**
	 * Instructs this <code>StatusEater</code> to persist the given
	 * <code>Status</code>, possibly
	 * including which <code>QueryItem</code>s were matched. The method of
	 * persistent storage (for
	 * example, a database or a text file) is chosen by the implementing class.
	 * 
	 * @param matches
	 * @param status
	 */
	public abstract void persist(List<QueryItem> matches, UserStatusData status);

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_SKIP_UNMATCHED_FMT = "Skipping persist() because there are not QueryItem matches for status id %ld";
	private static final String MSG_PROCESSING_FMT = "Entering process() for status id %ld";
}
