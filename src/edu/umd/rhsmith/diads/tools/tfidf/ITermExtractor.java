package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.Map;

/**
 * An interface defining a term-extraction tool. Such a
 * tool takes in a {@code String} for the text to be analyzed, and outputs a
 * mapping of term {@code String}s to {@code Double} values of the term's weight
 * in the given analysis text. These weight values may be the number of times
 * the term appears in the text, or another value such as their TF-IDF weight
 * in a given document set.
 * 
 * @author rmachedo
 * 
 */
public interface ITermExtractor {

	/**
	 * Compute and return a term-weight mapping for the given {@code String} of
	 * text. The result is a {@code Map<String, Double>} representing the
	 * weight of each term extracted from the text.
	 * 
	 * @param sentimentAnalysisText
	 *            the text {@code String} to analyze
	 * @return the computed {@code Map<String, Double>} representing the
	 *         weight of each term extracted from the text
	 */
	public Map<String, Double> process(String analysisText);

}
