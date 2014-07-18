package edu.umd.rhsmith.diads.meater.modules.tweater.media;

import java.util.Comparator;

public class UserIdComparator implements Comparator<UserData> {

	@Override
	public int compare(UserData o1, UserData o2) {
		return compareIds(o1, o2);
	}

	public static int compareIds(UserData o1, UserData o2) {
		if (o1 == null && o2 == null) {
			return 0;
		} else if (o1 != null && o2 == null) {
			return 1;
		} else if (o1 == null && o2 != null) {
			return -1;
		} else if (o1.getUserId() > o2.getUserId()) {
			return 1;
		} else if (o1.getUserId() < o2.getUserId()) {
			return -1;
		} else {
			return 0;
		}
	}
}
