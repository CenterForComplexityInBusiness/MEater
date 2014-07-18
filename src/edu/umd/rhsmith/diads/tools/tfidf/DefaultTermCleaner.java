package edu.umd.rhsmith.diads.tools.tfidf;

public class DefaultTermCleaner implements TermCleaner {

	@Override
	public String clean(String analysisText) {
		return analysisText.replaceAll("[^\\w\\d\\s]", "");
	}

}
