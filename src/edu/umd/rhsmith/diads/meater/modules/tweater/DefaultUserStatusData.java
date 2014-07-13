package edu.umd.rhsmith.diads.meater.modules.tweater;

import java.util.Date;

import twitter4j.Status;

public class DefaultUserStatusData implements UserStatusData {

	private final UserData userData;
	private final StatusData statusData;

	public DefaultUserStatusData(UserData userData, StatusData statusData) {
		this.userData = userData;
		this.statusData = statusData;
	}

	public DefaultUserStatusData(Status baseStatus) {
		this.userData = new DefaultUserData(baseStatus.getUser());
		this.statusData = new DefaultStatusData(baseStatus);
	}

	@Override
	public long getUserId() {
		return this.userData.getUserId();
	}

	@Override
	public String getUserName() {
		return this.userData.getUserName();
	}

	@Override
	public String getUserScreenName() {
		return this.userData.getUserScreenName();
	}

	@Override
	public String getUserLanguage() {
		return this.userData.getUserLanguage();
	}

	@Override
	public Date getUserCreatedAt() {
		return this.userData.getUserCreatedAt();
	}

	@Override
	public String getUserDescription() {
		return this.userData.getUserDescription();
	}

	@Override
	public String getUserLocation() {
		return this.userData.getUserLocation();
	}

	@Override
	public int getUserUtcOffset() {
		return this.userData.getUserUtcOffset();
	}

	@Override
	public boolean isUserVerified() {
		return this.userData.isUserVerified();
	}

	@Override
	public int getUserFollowersCount() {
		return this.userData.getUserFollowersCount();
	}

	@Override
	public int getUserFriendsCount() {
		return this.userData.getUserFriendsCount();
	}

	@Override
	public long getStatusId() {
		return this.statusData.getStatusId();
	}

	@Override
	public String getStatusText() {
		return this.statusData.getStatusText();
	}

	@Override
	public String getMatchableStatusText() {
		return this.statusData.getMatchableStatusText();
	}

	@Override
	public Date getStatusCreatedAt() {
		return this.statusData.getStatusCreatedAt();
	}

	@Override
	public double getStatusLatitude() {
		return this.statusData.getStatusLatitude();
	}

	@Override
	public double getStatusLongitude() {
		return this.statusData.getStatusLongitude();
	}

	@Override
	public boolean isStatusTruncated() {
		return this.statusData.isStatusTruncated();
	}

	@Override
	public boolean isStatusPossiblySensitive() {
		return this.statusData.isStatusPossiblySensitive();
	}

	@Override
	public boolean isStatusRetweet() {
		return this.statusData.isStatusRetweet();
	}

	@Override
	public int getStatusRetweetCount() {
		return this.statusData.getStatusRetweetCount();
	}

	@Override
	public long getStatusRetweetedStatusId() {
		return this.statusData.getStatusRetweetedStatusId();
	}

	@Override
	public long getStatusRetweetedUserId() {
		return this.statusData.getStatusRetweetedUserId();
	}

	@Override
	public long getStatusInReplyToStatusId() {
		return this.statusData.getStatusInReplyToStatusId();
	}

	@Override
	public long getStatusInReplyToUserId() {
		return this.statusData.getStatusInReplyToUserId();
	}

	@Override
	public boolean isSentimentAnalyzed() {
		return this.statusData.isSentimentAnalyzed();
	}

	@Override
	public String getSentimentAnalysisText() {
		return this.statusData.getSentimentAnalysisText();
	}

	@Override
	public double getSentiment() {
		return this.statusData.getSentiment();
	}

	@Override
	public void setSentiment(double sentiment) {
		this.statusData.setSentiment(sentiment);
	}

	@Override
	public void clearSentiment() {
		this.statusData.clearSentiment();
	}

	@Override
	public int getUserStatusesCount() {
		return this.userData.getUserStatusesCount();
	}

	@Override
	public int getUserListedCount() {
		return this.userData.getUserListedCount();
	}
}
