package edu.umd.rhsmith.diads.tools.tfidf;

/**
 * <p>
 * A default implementation of {@link TermCleaner}. Cleaned text is converted to
 * lowercase with all non-alphanumeric characters removed, preserving whitespace
 * boundaries between words. The cleaning operation used is also available as a
 * static method via {@link #clean(String)}.
 * </p>
 * 
 * @author rmachedo
 * 
 */
public class DefaultTermCleaner implements TermCleaner {

	@Override
	public String clean(String analysisText) {
		return cleanText(analysisText);
	}

	/**
	 * Return the given text converted to lowercase with all
	 * non-alphanumeric characters removed,
	 * preserving whitespace boundaries between words
	 * 
	 * @param analysisText
	 * @return
	 */
	public static String cleanText(String analysisText) {
		return analysisText.replaceAll("[^\\w\\d\\s]", "").toLowerCase();
	}

}
