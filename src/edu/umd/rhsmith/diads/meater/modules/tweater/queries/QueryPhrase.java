package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import edu.umd.rhsmith.diads.meater.modules.tweater.StatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.streaming.FilterQueryBuilder;

/**
 * A <code>QueryItem</code> that matches exactly on a sequence of keywords given
 * as a single <code>String</code>.
 * 
 * @author dmonner
 */
public class QueryPhrase extends QueryItem {

	/**
	 * The phrase to match.
	 */
	private final String phrase;

	private final String readableString;

	/**
	 * Creates a new <code>QueryPhrase</code> with the given
	 * unique ID, and the phrase
	 * that we wish to find.
	 * 
	 * @param id
	 * @param string
	 */
	public QueryPhrase(long id, String string) {
		super(id);
		this.phrase = string.trim().toLowerCase();
		this.readableString = String.format("phrase %ld [\"%s\"]",
				this.getQueryId(), this.phrase);
	}

	public String getPhrase() {
		return phrase;
	}

	@Override
	public boolean matches(final StatusData status) {
		return status.getMatchableStatusText().contains(phrase);
	}

	@Override
	public void addToFilterQuery(FilterQueryBuilder streamQuery) {
		streamQuery.addTrack(phrase);
	}

	@Override
	public String toString() {
		return this.readableString;
	}
}
