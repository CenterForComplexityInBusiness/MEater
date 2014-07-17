package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import edu.umd.rhsmith.diads.meater.modules.tweater.media.StatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.streaming.FilterQueryBuilder;

/**
 * Represents some property that can be matched in a <code>Status</code> object.
 * Many <code>QueryItem</code>s can belong to the same group, thus associating
 * them with each other when
 * persisted; one might want to do this for related variations on a query, such
 * as "Ma Bell" and
 * "AT&amp;T".
 * 
 * @author dmonner
 */
public abstract class QueryItem implements Comparable<QueryItem> {

	/**
	 * A unique ID number for this <code>QueryItem</code>
	 */
	private final long queryId;

	/**
	 * Creates a <code>QueryItem</code> with the specified
	 * unique ID.
	 * 
	 * @param id
	 */
	public QueryItem(long id) {
		this.queryId = id;
	}

	public long getQueryId() {
		return this.queryId;
	}
	
	/**
	 * Determines whether the given status matches this <code>QueryItem</code>.
	 * 
	 * @param status
	 * @return <code>true</code> iff the given status matches this
	 *         <code>QueryItem</code>
	 */
	public abstract boolean matches(StatusData status);

	/**
	 * Applies this <code>QueryItem</code> to the given
	 * <code>FilterQueryBuilder</code>, if applicable.
	 * 
	 * @param filterQuery
	 */
	public abstract void addToFilterQuery(FilterQueryBuilder filterQuery);

	@Override
	public int compareTo(QueryItem that) {
		return this.toString().compareTo(that.toString());
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof QueryItem) {
			return this.toString().equals(other.toString());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}
