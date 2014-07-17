package edu.umd.rhsmith.diads.meater.modules.tweater.media;

import java.util.Date;

import twitter4j.User;

public class DefaultUserData implements UserData {

	private final User user;

	public DefaultUserData(User user) {
		this.user = user;
	}

	@Override
	public long getUserId() {
		return this.user.getId();
	}

	@Override
	public String getUserName() {
		return this.user.getName();
	}

	@Override
	public String getUserScreenName() {
		return this.user.getScreenName();
	}

	@Override
	public String getUserLanguage() {
		return this.user.getLang();
	}

	@Override
	public Date getUserCreatedAt() {
		return this.user.getCreatedAt();
	}

	@Override
	public String getUserDescription() {
		return this.user.getDescription();
	}

	@Override
	public String getUserLocation() {
		return this.user.getLocation();
	}

	@Override
	public int getUserUtcOffset() {
		return this.user.getUtcOffset();
	}

	@Override
	public boolean isUserVerified() {
		return this.user.isVerified();
	}

	@Override
	public int getUserFollowersCount() {
		return this.user.getFollowersCount();
	}

	@Override
	public int getUserFriendsCount() {
		return this.user.getFriendsCount();
	}

	@Override
	public int getUserStatusesCount() {
		return this.user.getStatusesCount();
	}

	@Override
	public int getUserListedCount() {
		return this.user.getListedCount();
	}

}
