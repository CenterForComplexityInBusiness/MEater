package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.List;

/**
 * An interface defining a tool to split text strings into
 * individual terms.
 * 
 * @author rmachedo
 * 
 */
public interface TermSplitter {

	/**
	 * Splits the given analysis text {@link String} into a {@link List} of its
	 * component terms.
	 * 
	 * @param analysisText
	 *            the text to split into terms
	 * @return the {@link List} of terms the text was split into
	 */
	public List<String> getTerms(String analysisText);

}
