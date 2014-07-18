package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.List;

public interface TermFilter {
	public List<String> filterTerms(List<String> terms);
}
