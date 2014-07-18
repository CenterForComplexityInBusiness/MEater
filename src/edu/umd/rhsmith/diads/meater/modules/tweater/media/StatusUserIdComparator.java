package edu.umd.rhsmith.diads.meater.modules.tweater.media;

import java.util.Comparator;

public class StatusUserIdComparator implements Comparator<StatusData> {

	@Override
	public int compare(StatusData o1, StatusData o2) {
		return compareIds(o1, o2);
	}

	public static int compareIds(StatusData o1, StatusData o2) {
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
