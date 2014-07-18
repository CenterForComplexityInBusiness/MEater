package edu.umd.rhsmith.diads.meater.modules.common.tfidf;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SimpleTermExtractor extends BaseTermExtractor {

	@Override
	protected List<String> splitText(String analysisText) {
		return Arrays.asList(analysisText.split("\\s"));
	}

	@Override
	protected void recordTerm(Map<String, Double> frequencies, String term) {
		Double val = frequencies.get(term);
		if (val == null) {
			val = 0.0;
		}
		frequencies.put(term, val + 1.0);
	}

	@Override
	protected String cleanTerm(String term) {
		return term;
	}

	@Override
	protected String cleanText(String analysisText) {
		return analysisText.toLowerCase().replaceAll("[^\\w\\d\\s]", "");
	}

}
