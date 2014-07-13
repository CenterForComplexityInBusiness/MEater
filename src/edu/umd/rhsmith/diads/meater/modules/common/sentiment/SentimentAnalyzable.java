package edu.umd.rhsmith.diads.meater.modules.common.sentiment;

public interface SentimentAnalyzable {
	public boolean isSentimentAnalyzed();
	
	public String getSentimentAnalysisText();

	public double getSentiment();

	public void setSentiment(double sentiment);
	
	public void clearSentiment();
}
