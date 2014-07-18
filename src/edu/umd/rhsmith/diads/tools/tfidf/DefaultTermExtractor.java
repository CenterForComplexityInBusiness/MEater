package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DefaultTermExtractor implements ITermExtractor {

	private TermCleaner textCleaner;
	private TermSplitter splitter;
	private TermFilter filter;

	public DefaultTermExtractor(TermCleaner cleaner, TermSplitter splitter,
			TermFilter filter) {
		this.textCleaner = cleaner;
		this.splitter = splitter;
		this.filter = filter;
	}

	public DefaultTermExtractor() {
		this.textCleaner = new DefaultTermCleaner();
		this.splitter = new DefaultTermSplitter();
		this.filter = new DefaultTermFilter();
	}

	public TermCleaner getTextCleaner() {
		return textCleaner;
	}

	public void setTextCleaner(TermCleaner cleaner) {
		this.textCleaner = cleaner;
	}

	public TermSplitter getSplitter() {
		return splitter;
	}

	public void setSplitter(TermSplitter splitter) {
		this.splitter = splitter;
	}

	public TermFilter getFilter() {
		return filter;
	}

	public void setFilter(TermFilter filter) {
		this.filter = filter;
	}

	@Override
	public Map<String, Double> process(String analysisText) {
		Map<String, Double> tf = new TreeMap<String, Double>();
		if (analysisText == null) {
			return tf;
		}

		analysisText = cleanText(analysisText);
		if (analysisText == null) {
			return tf;
		}

		List<String> terms = getTerms(analysisText);
		if (terms == null) {
			return tf;
		}

		terms = filterTerms(terms);
		if (terms == null) {
			return tf;
		}

		for (String term : terms) {
			if (term != null) {
				Double val = tf.get(term);
				if (val == null) {
					val = 0.0;
				}
				tf.put(term, val + 1.0);
			}
		}

		return tf;
	}

	protected String cleanText(String analysisText) {
		if (this.textCleaner != null) {
			analysisText = textCleaner.clean(analysisText);
		}
		return analysisText;
	}

	protected List<String> getTerms(String analysisText) {
		List<String> terms = null;
		if (this.splitter != null) {
			terms = splitter.getTerms(analysisText);
		}
		return terms;
	}

	protected List<String> filterTerms(List<String> terms) {
		if (this.filter != null) {
			terms = filter.filterTerms(terms);
		}
		return terms;
	}
}
