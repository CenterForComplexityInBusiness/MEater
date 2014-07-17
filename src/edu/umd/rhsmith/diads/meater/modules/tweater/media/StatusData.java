package edu.umd.rhsmith.diads.meater.modules.tweater.media;

import java.util.Date;

import twitter4j.HashtagEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import edu.umd.rhsmith.diads.meater.modules.common.sentiment.SentimentAnalyzable;

public interface StatusData extends SentimentAnalyzable {
	public long getStatusId();

	public long getUserId();

	public String getStatusText();

	public String getMatchableStatusText();

	public Date getStatusCreatedAt();

	public double getStatusLatitude();

	public double getStatusLongitude();

	public boolean isStatusTruncated();

	public boolean isStatusPossiblySensitive();

	public boolean isStatusRetweet();

	public int getStatusRetweetCount();

	public long getStatusRetweetedStatusId();

	public long getStatusRetweetedUserId();

	public long getStatusInReplyToStatusId();

	public long getStatusInReplyToUserId();

	public HashtagEntity[] getHashtagEntities();

	public URLEntity[] getURLEntities();

	public UserMentionEntity[] getUserMentionEntities();
}
