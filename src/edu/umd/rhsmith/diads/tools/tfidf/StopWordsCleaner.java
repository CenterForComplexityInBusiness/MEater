package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.Collection;
import java.util.HashSet;

public class StopWordsCleaner implements TermCleaner {

	private final HashSet<String> stopWords;

	public StopWordsCleaner(Collection<String> stopWords) {
		this.stopWords = new HashSet<String>(stopWords);
	}

	@Override
	public String clean(String analysisText) {
		if (stopWords.contains(analysisText)) {
			return null;
		} else {
			return analysisText;
		}
	}
}
