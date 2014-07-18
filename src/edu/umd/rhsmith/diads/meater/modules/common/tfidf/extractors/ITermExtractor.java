package edu.umd.rhsmith.diads.meater.modules.common.tfidf.extractors;

import java.util.Map;

public interface ITermExtractor {
	
	public Map<String, Double> process(String analysisText);
}
