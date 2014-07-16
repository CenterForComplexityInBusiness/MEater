package edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import twitter4j.GeoLocation;
import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryFollow;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItemTime;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryLocation;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryPhrase;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QuerySource;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryTrack;
import edu.umd.rhsmith.diads.meater.util.NumberSet;
import edu.umd.rhsmith.diads.meater.util.Util;

/**
 * Reads query items from a MySQL database.
 * 
 * @author dmonner
 */
public class MySQLQueryBuilder extends QuerySource {

	private final String dbName;

	/**
	 * The MySQL connection pool
	 */
	private DataSource ds;

	/**
	 * The SQL WHERE string that specifies query groups
	 */
	private final String where;
	/**
	 * The maximum number of tries to try a transaction before giving up
	 */
	private final int maxtries = 5;

	public MySQLQueryBuilder(MySqlQuerySourceInitializer init)
			throws MEaterConfigurationException {
		super(init);

		this.dbName = init.getDbName();

		final String querygroups = init.getQueryGroups();
		if (querygroups != null && !querygroups.trim().isEmpty()) {
			where = " WHERE "
					+ new NumberSet(querygroups).toSQL("query_group_no");
		} else {
			where = "";
		}
	}

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
		super.doInitRoutine();

		this.ds = this.getComponentManager().getMain().getSqlManager()
				.getDataSource(dbName);
		if (this.ds == null) {
			throw new MEaterConfigurationException(this.messageString(
					"Couldn't get data source from manager with name '%s'",
					dbName));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.umd.cs.dmonner.tweater.QueryBuilder#update()
	 */
	@Override
	public List<QueryItemTime> getQueriesFromSource() {
		logFine("Beginning MySQLQueryBuilder update...");
		final List<QueryItemTime> all = new LinkedList<QueryItemTime>();
		boolean querySucceeded = false;

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int tries = 0;

		try {
			// build a new tree of all query items
			while (conn == null && tries++ < maxtries) {
				conn = ds.getConnection();
			}

			if (conn == null) {
				return null;
			}

			stmt = conn.createStatement();

			// get the track queries
			rs = stmt
					.executeQuery("SELECT query_group_no, query_track_no, query_track_string, query_start_time, query_end_time "
							+ "FROM query_group INNER JOIN query_track USING (query_group_no)"
							+ where + ";");

			while (rs.next()) {
				all.add(new QueryItemTime(new QueryTrack(rs
						.getInt("query_group_no"), rs
						.getString("query_track_string")), rs
						.getLong("query_start_time"), rs
						.getLong("query_end_time")));
			}

			// get the phrase queries
			rs = stmt
					.executeQuery("SELECT query_group_no, query_phrase_no, query_phrase_string, query_start_time, query_end_time "
							+ "FROM query_group INNER JOIN query_phrase USING (query_group_no)"
							+ where + ";");

			while (rs.next()) {
				all.add(new QueryItemTime(new QueryPhrase(rs
						.getInt("query_group_no"), rs
						.getString("query_phrase_string")), rs
						.getLong("query_start_time"), rs
						.getLong("query_end_time")));
			}

			// get the follow queries
			rs = stmt
					.executeQuery("SELECT query_group_no, query_follow_no, query_user_id, query_start_time, query_end_time "
							+ "FROM query_group INNER JOIN query_follow USING (query_group_no)"
							+ where + ";");

			while (rs.next()) {
				all.add(new QueryItemTime(
						new QueryFollow(rs.getInt("query_group_no"), rs
								.getLong("query_user_id")), rs
								.getLong("query_start_time"), rs
								.getLong("query_end_time")));
			}

			// get the location queries
			rs = stmt
					.executeQuery("SELECT query_group_no, query_location_no, "
							+ "query_location_longSW, query_location_latSW, query_location_longNE, query_location_latNE, "
							+ "query_start_time, query_end_time "
							+ "FROM query_group INNER JOIN query_location USING (query_group_no)"
							+ where + ";");

			while (rs.next()) {
				double longSW = rs.getDouble("query_location_longSW"), latSW = rs
						.getDouble("query_location_latSW"), longNE = rs
						.getDouble("query_location_longNE"), latNE = rs
						.getDouble("query_location_latNE");
				GeoLocation pointSW = new GeoLocation(latSW, longSW);
				GeoLocation pointNE = new GeoLocation(latNE, longNE);
				QueryLocation ql = null;

				try {
					ql = new QueryLocation(rs.getInt("query_group_no"),
							pointSW, pointNE);
				} catch (IllegalArgumentException ex) {
					logWarning("Unable to construct location query #"
							+ rs.getInt("query_location_no") + ": "
							+ ex.getMessage());
				}
				if (ql != null) {
					all.add(new QueryItemTime(ql, rs
							.getLong("query_start_time"), rs
							.getLong("query_end_time")));
				}
			}

			querySucceeded = true;
		} catch (final SQLException ex) {
			logSevere("SQLState: " + ex.getSQLState() + "\n" + //
					"VendorError: " + ex.getErrorCode() + "\n" + //
					Util.traceMessage(ex));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (final SQLException ex) {
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (final SQLException ex) {
				} finally {
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (final SQLException ex) {
					}
				}
			}
		}

		if (querySucceeded) {
			logFine("Completed MySQLQueryBuilder update.");
			return all;
		} else {
			logWarning("MySQLQueryBuilder update FAILED!");
			return null;
		}
	}
}
