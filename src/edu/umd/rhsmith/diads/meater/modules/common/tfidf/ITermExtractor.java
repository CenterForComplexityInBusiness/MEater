package edu.umd.rhsmith.diads.meater.modules.common.tfidf;

import java.util.Map;

public interface ITermExtractor {
	
	public Map<String, Double> process(String analysisText);
}
