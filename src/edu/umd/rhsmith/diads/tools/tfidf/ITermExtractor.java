package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.Map;

public interface ITermExtractor {
	
	public Map<String, Double> process(String analysisText);
}
