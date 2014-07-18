package edu.umd.rhsmith.diads.meater.modules.tweater.media;

import java.util.Comparator;

public class UserScreenNameComparator implements Comparator<UserData> {

	@Override
	public int compare(UserData o1, UserData o2) {
		return compareNames(o1, o2);
	}

	public static int compareNames(UserData o1, UserData o2) {
		if (o1 == null && o2 == null) {
			return 0;
		} else if (o1 != null && o2 == null) {
			return 1;
		} else if (o1 == null && o2 != null) {
			return -1;
		} else {
			return o1.getUserScreenName().compareTo(o2.getUserScreenName());
		}
	}
}
