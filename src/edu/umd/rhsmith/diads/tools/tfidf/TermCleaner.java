package edu.umd.rhsmith.diads.tools.tfidf;

/**
 * An interface defining a tool to clean text for term-extraction.
 * 
 * @author rmachedo
 * 
 */
public interface TermCleaner {

	/**
	 * Perform the cleaning operation defined by this {@code TermCleaner} on the
	 * given text {@code String}, returning the result of the cleaning
	 * operation. May return <code>null</code> to indicate that the term should
	 * be discarded.
	 * 
	 * @param analysisText
	 *            the text {@code String} to clean
	 * @return the cleaned version of the text, or <code>null</code> to indicate
	 *         that the term should be discarded
	 */
	public String clean(String analysisText);

}
