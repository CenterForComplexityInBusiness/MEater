package edu.umd.rhsmith.diads.meater.modules.tweater.streaming;

import java.util.LinkedList;
import java.util.List;

import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;
import edu.umd.rhsmith.diads.meater.util.Util;
import twitter4j.FilterQuery;
import twitter4j.GeoLocation;

public class FilterQueryBuilder {

	private List<String> tracks;
	private LinkedList<Long> follows;
	private LinkedList<double[]> locations;

	public FilterQueryBuilder() {
		this.tracks = new LinkedList<String>();
		this.follows = new LinkedList<Long>();
		this.locations = new LinkedList<double[]>();
	}

	public FilterQueryBuilder(Iterable<QueryItem> queryItems) {
		this();
		this.addAll(queryItems);
	}

	public void addAll(Iterable<QueryItem> queryItems) {
		for (QueryItem q : queryItems) {
			q.addToFilterQuery(this);
		}
	}

	public void addTrack(String query) {
		tracks.add(query);
	}

	public void addFollow(long query) {
		follows.add(query);
	}

	public void addLocation(GeoLocation pointSW, GeoLocation pointNE) {
		locations.add(new double[] { pointSW.getLongitude(),
				pointSW.getLatitude() });
		locations.add(new double[] { pointNE.getLongitude(),
				pointNE.getLatitude() });
	}

	public FilterQuery getFilterQuery() {
		String[] trackA = new String[tracks.size()];
		long[] followA = new long[follows.size()];
		double[][] locationsA = new double[locations.size()][];

		FilterQuery fq = new FilterQuery();
		fq.track(trackA);
		fq.follow(followA);
		fq.locations(locationsA);
		return fq;
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("{Track&Phrase=");
		sb.append("[");
		sb.append(Util.joinStrings(tracks, ", "));
		sb.append("]");

		sb.append(", Follow=");
		sb.append("[");
		sb.append(Util.joinStrings(follows, ", "));
		sb.append("]");

		sb.append(", Location=");
		sb.append("[");
		boolean first = true;
		for (double[] point : locations) {
			if (!first) {
				sb.append(", ");
			}
			sb.append('(');
			sb.append(point[0]);
			sb.append(',');
			sb.append(point[1]);
			sb.append(')');
			first = false;
		}
		sb.append("]");

		sb.append("}");

		return sb.toString();
	}
}
