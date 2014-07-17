package edu.umd.rhsmith.diads.meater.modules.tweater.queries;

import twitter4j.GeoLocation;
import edu.umd.rhsmith.diads.meater.modules.tweater.media.StatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.streaming.FilterQueryBuilder;

/**
 * A <code>QueryItem</code> that matches tweets on a latitude/longitude bounding
 * box.
 * 
 * @author rmachedo
 */
public class QueryLocation extends QueryItem {

	/**
	 * Coordinates of the bounding-box for the location being searched for
	 */
	private final GeoLocation pointSW;
	private final GeoLocation pointNE;

	private final String readableString;

	/**
	 * Creates a new <code>QueryLocation</code> with the given
	 * unique
	 * ID, and the location bounding-box that we wish to find defined by it
	 * south-west
	 * and north-east longitude/latitude points.
	 * 
	 * @param id
	 * @param pointSW
	 * @param pointNE
	 * 
	 * @throws IllegalArgumentException
	 *             If the south-west point is not to the south and west
	 *             of the north-east point, or if the points span outside of
	 *             <code>[-90, 90]</code> on
	 *             latitude or <code>[-180, 180]</code> on longitude.
	 */
	public QueryLocation(long id, GeoLocation pointSW, GeoLocation pointNE) {
		super(id);

		if (pointSW.getLatitude() >= pointNE.getLatitude()
				|| pointSW.getLongitude() >= pointNE.getLongitude()) {
			throw new IllegalArgumentException(String.format(
					MSG_ERR_ORIENTATION_FMT, pointSW, pointNE));
		} else if (pointSW.getLatitude() > 90 || pointSW.getLatitude() < -90) {
			throw new IllegalArgumentException(String.format(
					MSG_ERR_SW_LAT_FMT, pointSW));
		} else if (pointSW.getLongitude() > 180
				|| pointSW.getLongitude() < -180) {
			throw new IllegalArgumentException(String.format(
					MSG_ERR_SW_LONG_FMT, pointSW));
		} else if (pointNE.getLatitude() > 90 || pointNE.getLatitude() < -90) {
			throw new IllegalArgumentException(String.format(
					MSG_ERR_NE_LAT_FMT, pointNE));
		} else if (pointNE.getLongitude() > 180
				|| pointNE.getLongitude() < -180) {
			throw new IllegalArgumentException(String.format(
					MSG_ERR_NE_LONG_FMT, pointNE));
		}

		this.pointNE = pointNE;
		this.pointSW = pointSW;
		this.readableString = String.format("location %ld [%s, %s]", this
				.getQueryId(), pointSW.toString(), pointNE.toString());
	}

	public GeoLocation getPointSW() {
		return pointSW;
	}

	public GeoLocation getPointNE() {
		return pointNE;
	}

	@Override
	public boolean matches(final StatusData status) {
		return status.getStatusLatitude() >= pointSW.getLatitude()
				&& status.getStatusLatitude() <= pointNE.getLatitude()
				&& status.getStatusLongitude() >= pointSW.getLongitude()
				&& status.getStatusLongitude() < pointNE.getLongitude();
	}

	@Override
	public String toString() {
		return this.readableString;
	}

	@Override
	public void addToFilterQuery(FilterQueryBuilder streamQuery) {
		streamQuery.addLocation(this.pointSW, this.pointNE);
	}

	private static final String MSG_ERR_NE_LONG_FMT = "North-east point exceeded [-180, 180] longitude bounds: %s";
	private static final String MSG_ERR_NE_LAT_FMT = "North-east point exceeded [-90, 90] latitude bounds: %s - Twitter will not like this!";
	private static final String MSG_ERR_SW_LONG_FMT = "South-west point exceeded [-180, 180] longitude bounds: %s - Twitter will not like this!";
	private static final String MSG_ERR_SW_LAT_FMT = "South-west point exceeded [-90, 90] latitude bounds: %s - Twitter will not like this!";
	private static final String MSG_ERR_ORIENTATION_FMT = "South-west point was not to the south and west of north-east point: (%s, %s) - Twitter will not like this!";
}
