package edu.umd.rhsmith.diads.tools.twitter;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.TwitterException;

public interface TimelineStreamListener {
	public void onUserStarted(long userId);
	
	public void onUserPageStarted(long userId, int page);
	
	public void onRateLimit(long userId, int page, RateLimitStatus rateLimitStatus);
	
	public void onException(long userId, int page, TwitterException ex);
	
	public void onStatus(Status status);
	
	public void onShutdown();
}
