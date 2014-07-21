package edu.umd.rhsmith.diads.tools.sentiment;

/**
 * An interface defining a sentiment-analysis tool. Such a
 * tool takes in a {@code String} for the text to be analyzed, and outputs a
 * {@code double} value representing on a scale from {@code [-1.0, 1.0]} the
 * computed sentiment of the text (with more negative values corresponding to
 * more negative sentiment, and more positive values corresponding to more
 * positive sentiment). A value of {@code 0.0} may be used to
 * indicate that sentiment could not be computed.
 * 
 * @author dmonner
 * 
 */
public interface ISentimentAnalyzer {

	/**
	 * Compute and return a sentiment score for the given {@code String} of
	 * text. The result is a {@code double} value representing on a scale from
	 * {@code [-1.0, 1.0]} the
	 * computed sentiment of the text (with more negative values corresponding
	 * to
	 * more negative sentiment, and more positive values corresponding to more
	 * positive sentiment). A value of exactly {@code 0.0} may be used to
	 * indicate that sentiment could not be computed.
	 * 
	 * @param sentimentAnalysisText
	 *            the text {@code String} to analyze
	 * @return the computed sentiment score between {@code [-1.0, 1.0]}, or
	 *         exactly {@code 0.0} to
	 *         indicate that sentiment was perfectly neutral or could not be
	 *         computed
	 */
	public double process(String sentimentAnalysisText);
}
