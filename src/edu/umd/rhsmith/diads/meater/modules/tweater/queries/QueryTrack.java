package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import java.util.Arrays;

import edu.umd.rhsmith.diads.meater.modules.tweater.StatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.streaming.FilterQueryBuilder;

/**
 * A <code>QueryItem</code> that matches on set of keywords, which need not
 * necessarily be in order
 * or adjacent, specified as a single whitespace-separated <code>String</code>.
 * 
 * @author dmonner
 */
public class QueryTrack extends QueryItem {

	/**
	 * Individual keywords being tracked, as extracted from the input string.
	 */
	private final String[] trackWords;

	/**
	 * The original tracking string
	 */
	private final String originalString;

	private final String readableString;

	/**
	 * Creates a new <code>QueryTrack</code> with the given unique
	 * ID, and the
	 * whitespace-separated keywords that we wish to find.
	 * 
	 * @param id
	 * @param string
	 */
	public QueryTrack(long id, String string) {
		super(id);
		this.originalString = string.trim().toLowerCase();
		this.trackWords = this.originalString.split("\\s");
		this.readableString = String.format("track %ld [%s]", this.getQueryId(),
				this.originalString);
	}

	public String[] getTrackWords() {
		return Arrays.copyOf(this.trackWords, this.trackWords.length);
	}

	@Override
	public boolean matches(final StatusData status) {
		for (final String word : trackWords) {
			if (!status.getMatchableStatusText().contains(word)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void addToFilterQuery(FilterQueryBuilder streamQuery) {
		for (String word : this.trackWords) {
			streamQuery.addTrack(word);
		}
	}

	@Override
	public String toString() {
		return this.readableString;
	}
}
