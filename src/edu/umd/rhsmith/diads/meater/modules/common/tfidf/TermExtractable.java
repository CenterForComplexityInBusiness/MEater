package edu.umd.rhsmith.diads.meater.modules.common.tfidf;

import java.util.Map;

public interface TermExtractable {
	public boolean isTermExtracted();

	public String getTermExtractionText();

	public Map<String, Double> getTermFrequencies();

	public void setTerms(Map<String, Double> terms);

	public void clearTermExctraction();
}
