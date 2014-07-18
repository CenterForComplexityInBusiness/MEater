package edu.umd.rhsmith.diads.meater.modules.common.tfidf;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class BaseTermExtractor implements ITermExtractor {

	@Override
	public Map<String, Double> process(String analysisText) {
		Map<String, Double> tf = new TreeMap<String, Double>();

		analysisText = cleanText(analysisText);
		List<String> terms = splitText(analysisText);
		for (String term : terms) {
			term = cleanTerm(term);
			if (term != null) {
				recordTerm(tf, term);
			}
		}

		return tf;
	}

	protected abstract List<String> splitText(String analysisText);

	protected abstract void recordTerm(Map<String, Double> frequencies,
			String term);

	protected abstract String cleanTerm(String term);

	protected abstract String cleanText(String analysisText);

}
