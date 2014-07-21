package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.List;

/**
 * An interface defining a tool to filter term-lists generated
 * from text.
 * 
 * @author rmachedo
 * 
 */
public interface TermFilter {

	/**
	 * Perform the filter operation defined by this {@code TermCleaner} on the
	 * given {@link List} of term {@link String} instances, returning the result
	 * of the filter operation.
	 * 
	 * @param terms
	 *            the list of terms to filtered
	 * @return the filtered list of terms
	 */
	public List<String> filterTerms(List<String> terms);

}
