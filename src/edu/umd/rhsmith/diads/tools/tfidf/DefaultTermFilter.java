package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.LinkedList;
import java.util.List;

public class DefaultTermFilter implements TermFilter {

	private TermFilter preFilter;
	private TermCleaner cleaner;

	public DefaultTermFilter() {
		this.preFilter = null;
		this.cleaner = null;
	}

	public TermFilter getPreFilter() {
		return preFilter;
	}

	public void setPreFilter(TermFilter preFilter) {
		this.preFilter = preFilter;
	}

	public TermCleaner getCleaner() {
		return cleaner;
	}

	public void setCleaner(TermCleaner cleaner) {
		this.cleaner = cleaner;
	}

	@Override
	public List<String> filterTerms(List<String> terms) {
		if (this.preFilter != null) {
			terms = preFilter.filterTerms(terms);
		}

		if (this.cleaner != null) {
			List<String> cleanedTerms = new LinkedList<String>();
			for (String term : terms) {
				term = this.cleaner.clean(term);
				if (term != null) {
					cleanedTerms.add(term);
				}
			}
			terms = cleanedTerms;
		}

		return terms;
	}
}
