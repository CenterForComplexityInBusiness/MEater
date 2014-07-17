package edu.umd.rhsmith.diads.meater.modules.tweater.media;

import java.util.Date;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

public class DefaultStatusData implements StatusData {

	private boolean sentimentAnalyzed = false;
	private double sentiment = 0.0;

	private final Status source;
	private final String matchableStatusText;

	public DefaultStatusData(Status source) {
		this.source = source;
		this.matchableStatusText = this.source.getText().toLowerCase();
	}

	@Override
	public long getStatusId() {
		return source.getId();
	}

	@Override
	public long getUserId() {
		return source.getUser().getId();
	}

	@Override
	public String getStatusText() {
		return source.getText();
	}

	@Override
	public String getMatchableStatusText() {
		return this.matchableStatusText;
	}

	@Override
	public Date getStatusCreatedAt() {
		return source.getCreatedAt();
	}

	@Override
	public double getStatusLatitude() {
		return source.getGeoLocation() == null ? 0 : source.getGeoLocation()
				.getLatitude();
	}

	@Override
	public double getStatusLongitude() {
		return source.getGeoLocation() == null ? 0 : source.getGeoLocation()
				.getLongitude();
	}

	@Override
	public boolean isStatusTruncated() {
		return source.isTruncated();
	}

	@Override
	public boolean isStatusPossiblySensitive() {
		return source.isPossiblySensitive();
	}

	@Override
	public boolean isStatusRetweet() {
		return source.isRetweet();
	}

	@Override
	public int getStatusRetweetCount() {
		return source.getRetweetCount();
	}

	@Override
	public long getStatusRetweetedStatusId() {
		return source.getRetweetedStatus().getId();
	}

	@Override
	public long getStatusRetweetedUserId() {
		return source.getRetweetedStatus().getUser().getId();
	}

	@Override
	public long getStatusInReplyToStatusId() {
		return source.getInReplyToStatusId();
	}

	@Override
	public long getStatusInReplyToUserId() {
		return source.getInReplyToUserId();
	}

	@Override
	public String getSentimentAnalysisText() {
		return source.getText();
	}

	@Override
	public boolean isSentimentAnalyzed() {
		return sentimentAnalyzed;
	}

	@Override
	public double getSentiment() {
		return sentiment;
	}

	@Override
	public void setSentiment(double sentiment) {
		this.sentiment = sentiment;
		this.sentimentAnalyzed = true;
	}

	@Override
	public void clearSentiment() {
		this.sentiment = 0.0;
		this.sentimentAnalyzed = false;
	}

	@Override
	public HashtagEntity[] getHashtagEntities() {
		return this.source.getHashtagEntities();
	}

	@Override
	public URLEntity[] getURLEntities() {
		return this.source.getURLEntities();
	}

	@Override
	public UserMentionEntity[] getUserMentionEntities() {
		return this.source.getUserMentionEntities();
	}
}
