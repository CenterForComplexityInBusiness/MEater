package edu.umd.rhsmith.diads.meater.modules.tweater.media;

import java.util.Date;

public interface UserData {
	public long getUserId();

	public String getUserName();

	public String getUserScreenName();

	public String getUserLanguage();

	public Date getUserCreatedAt();

	public String getUserDescription();

	public String getUserLocation();

	public int getUserUtcOffset();

	public boolean isUserVerified();

	public int getUserFollowersCount();

	public int getUserFriendsCount();
	
	public int getUserStatusesCount();
	
	public int getUserListedCount();
}
