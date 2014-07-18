package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.Arrays;
import java.util.List;

public class DefaultTermSplitter implements TermSplitter {

	@Override
	public List<String> getTerms(String analysisText) {
		return Arrays.asList(analysisText.split("\\s"));
	}

}
