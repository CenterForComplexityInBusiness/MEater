package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import edu.umd.rhsmith.diads.meater.modules.tweater.media.StatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.streaming.FilterQueryBuilder;

/**
 * A <code>QueryItem</code> that matches on the user ID of the originator of the
 * <code>Status</code> .
 * 
 * @author dmonner
 */
public class QueryFollow extends QueryItem {

	/**
	 * The user ID whose <code>Status</code>es this object will match.
	 */
	private final long userId;

	private final String readableString;

	public long getUserId() {
		return userId;
	}

	/**
	 * Creates a new <code>QueryFollow</code> with the given
	 * unique ID and the user ID
	 * of the user we wish to follow.
	 * 
	 * @param id
	 * @param userId
	 */
	public QueryFollow(long id, long userId) {
		super(id);
		this.userId = userId;
		this.readableString = String.format("follow %d %d", this.getQueryId(),
				userId);
	}

	@Override
	public boolean matches(final StatusData status) {
		return status.getUserId() == userId;
	}

	@Override
	public String toString() {
		return this.readableString;
	}

	@Override
	public void addToFilterQuery(FilterQueryBuilder streamQuery) {
		streamQuery.addFollow(userId);
	}
}
