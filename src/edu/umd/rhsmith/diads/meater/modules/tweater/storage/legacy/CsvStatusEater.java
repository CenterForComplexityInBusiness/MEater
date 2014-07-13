package edu.umd.rhsmith.diads.meater.modules.tweater.storage.legacy;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentManager;
import edu.umd.rhsmith.diads.meater.modules.tweater.UserStatusData;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;
import edu.umd.rhsmith.diads.meater.util.ControlException;

/**
 * This class persists statuses to a local CSV file.
 * 
 * @author dmonner
 */
public class CsvStatusEater extends StatusEater {
	/**
	 * The handle on the output file
	 */
	private PrintWriter outfile;

	public CsvStatusEater(CsvStatusEaterInitializer init, ComponentManager mgr)
			throws MEaterConfigurationException {
		super(init, mgr);

		try {
			this.outfile = new PrintWriter(init.getFilename());
		} catch (FileNotFoundException e) {
			throw new MEaterConfigurationException(this.messageString(
					MSG_ERR_OUTFILE_FMT, e));
		}
	}

	@Override
	public void persist(final List<QueryItem> matches,
			final UserStatusData status) {
		// get sentiment information
		final double sentiment = status.getSentiment();

		// get location information
		final double lat = status.getStatusLatitude();
		final double lon = status.getStatusLongitude();
		final String locStr = status.getUserLocation() == null ? ""
				: scrub(status.getUserLocation());
		final String lang = status.getUserLanguage() == null ? ""
				: scrub(status.getUserLanguage());

		// get retweet information
		final boolean rt = status.isStatusRetweet();
		final long rtct = rt ? status.getStatusRetweetCount() : 0;
		final long rtid = rt ? status.getStatusRetweetedStatusId() : -1;

		// get match information
		final String matchString = matches.isEmpty() ? "" : scrub(matches
				.get(0).toString());

		synchronized (outfile) {
			this.outfile.println(status.getUserId() + ",\"" + //
					status.getUserScreenName() + "\",\"" + //
					locStr + "\"," + //
					status.getUserFollowersCount() + "," + //
					status.getUserFriendsCount() + "," + //
					status.getUserId() + "," + //
					status.getUserCreatedAt() + ",\"" + //
					scrub(status.getStatusText()) + "\"," + //
					sentiment + "," + //
					rt + "," + //
					rtid + "," + //
					rtct + "," + //
					lat + "," + //
					lon + "," + //
					status.getUserCreatedAt() + "," + //
					status.getUserStatusesCount() + "," + //
					status.getUserListedCount() + "," + //
					status.isUserVerified() + ",\"" + //
					lang + "\"," + //
					status.getUserUtcOffset() / 3600 + ",\"" + //
					matchString + "\",");

			this.outfile.flush();
		}
	}

	/*
	 * --------------------------------
	 * Control methods
	 * --------------------------------
	 */

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
		// print the column headers to the output file
		this.outfile.println("user_id, " + //
				"user_name," + //
				"user_location," + //
				"user_followers," + //
				"user_friends," + //
				"status_id," + //
				"status_date," + //
				"status_text," + //
				"status_sentiment," + //
				"status_is_retweet," + //
				"status_retweet_of," + //
				"status_retweet_count," + //
				"status_latitude," + //
				"status_longitude," + //
				"user_join_date," + //
				"user_status_count," + //
				"user_listed," + //
				"user_verified," + //
				"user_lang," + //
				"user_utc_offset," + //
				"matched");
	}

	@Override
	protected void doStartupRoutine() throws ControlException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doShutdownRoutine() {
		// TODO Auto-generated method stub

	}

	/*
	 * --------------------------------
	 * Misc. utilities
	 * --------------------------------
	 */

	/**
	 * Sanitize the input string for use in a single field in a CSV file by
	 * converting newlines to
	 * spaces and replacing double quotes with single quotes.
	 * 
	 * @param in
	 * @return The input string, sanitized for a CSV file.
	 */
	public static String scrub(final String in) {
		return in.replaceAll("\n", " ").replaceAll("\"", "'");
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_ERR_OUTFILE_FMT = "Unable to create output writer: %s";
}
