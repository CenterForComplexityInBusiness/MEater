package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.List;

public interface TermSplitter {

	public abstract List<String> getTerms(String analysisText);

}
