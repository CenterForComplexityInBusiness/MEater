package edu.umd.rhsmith.diads.meater.modules.tweater.queries.legacy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import twitter4j.GeoLocation;

import com.mdimension.jchronic.Chronic;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryFollow;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItem;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryItemTime;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryLocation;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryPhrase;
import edu.umd.rhsmith.diads.meater.modules.tweater.queries.QueryTrack;
import edu.umd.rhsmith.diads.meater.util.Util;

/**
 * Reads query items from a CSV file.
 * 
 * @author dmonner
 */
public class CsvQuerySource extends QuerySource {

	/**
	 * The file from which to read the query
	 */
	private final File infile;

	public CsvQuerySource(CsvQuerySourceInitializer init)
			throws MEaterConfigurationException {
		super(init);
		this.infile = new File(init.getFilename());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.umd.cs.dmonner.tweater.QueryBuilder#update()
	 */
	@Override
	public List<QueryItemTime> getQueriesFromSource() {
		logFine(MSG_READING_FILE_FMT, infile);

		final List<QueryItemTime> all = new LinkedList<QueryItemTime>();
		BufferedReader in = null;
		try {
			// get reader for file
			in = new BufferedReader(new FileReader(infile));

			String line = null;
			int lineno = 0;

			// read all lines in file
			while ((line = in.readLine()) != null) {
				lineno++;
				line = line.trim();

				// skip comment lines
				if (line.startsWith("#") || line.isEmpty())
					continue;

				final String[] fields = Util.splitUnlessQuoted(line, ",", "\"");

				// be sure we have the correct number of fields
				if (fields.length != 4) {
					logWarning(MSG_ERR_NUMFIELDS_FMT, fields.length, lineno);
					continue;
				}

				// extract the base fields
				final long start = Chronic.parse(
						Util.unquoteString(fields[0].trim())).getBegin() * 1000L;
				final long end = Chronic.parse(
						Util.unquoteString(fields[1].trim())).getEnd() * 1000L;
				final String type = Util.unquoteString(fields[2].trim());
				final String item = Util.unquoteString(fields[3].trim());

				// now try to build a query
				QueryItem qitem = null;
				if (type.equalsIgnoreCase("phrase"))
					qitem = new QueryPhrase(lineno, item);
				else if (type.equalsIgnoreCase("keywords")
						|| type.equalsIgnoreCase("keyword")
						|| type.equalsIgnoreCase("track"))
					qitem = new QueryTrack(lineno, item);
				else if (type.equalsIgnoreCase("user")
						|| type.equalsIgnoreCase("follow")) {
					try {
						qitem = new QueryFollow(lineno, Long.parseLong(item));
					} catch (final NumberFormatException ex) {
						logWarning(MSG_ERR_USERID_FMT, item, lineno);
					}
				} else if (type.equalsIgnoreCase("location")) {
					try {
						String[] coords = item.split(";");
						double longSW = Float.parseFloat(coords[0]), latSW = Float
								.parseFloat(coords[1]), longNE = Float
								.parseFloat(coords[2]), latNE = Float
								.parseFloat(coords[3]);
						GeoLocation pointSW = new GeoLocation(latSW, longSW);
						GeoLocation pointNE = new GeoLocation(latNE, longNE);
						qitem = new QueryLocation(lineno, pointSW, pointNE);
					} catch (final NumberFormatException ex) {
						logWarning(MSG_ERR_LOCATION_FMT, item, lineno);
					} catch (final ArrayIndexOutOfBoundsException ex) {
						logWarning(MSG_ERR_LOCATION_FMT, item, lineno);
					} catch (final IllegalArgumentException ex) {
						logWarning(MSG_ERR_ILLEGAL_FMT);
					}
				} else {
					logWarning(MSG_ERR_INVALID_TYPE_FMT, lineno);
				}

				if (qitem != null)
					all.add(new QueryItemTime(qitem, start, end));
			}

			logFine(MSG_UPDATE_COMPLETE);
			return all;
		} catch (final IOException ex) {
			logSevere(MSG_ERR_IO_FMT, infile.getPath(), Util.traceMessage(ex));
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
				}
			}
		}

		logWarning(MSG_ERR_FAILED_FMT, infile);
		return null;
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_READING_FILE_FMT = "Beginning CSVQueryBuilder update; reading file %s... ";
	private static final String MSG_UPDATE_COMPLETE = "Completed CSVQueryBuilder update.";
	private static final String MSG_ERR_FAILED_FMT = "CSVQueryBuilder update FAILED from file %s!";
	private static final String MSG_ERR_NUMFIELDS_FMT = "Malformed input! Expected 4 fields, found %, line number %d";
	private static final String MSG_ERR_INVALID_TYPE_FMT = "Malformed input! Type of query must be \"phrase\", \"keywords\", or \"user\" at line number %d";
	private static final String MSG_ERR_USERID_FMT = "Malformed input! Expected a user id number (not \"%s\") on line number %d";
	private static final String MSG_ERR_ILLEGAL_FMT = "Illegal input! %s on line number %d";
	private static final String MSG_ERR_LOCATION_FMT = "Malformed input! Expected four semicolon-delimeted coordinates (not \"%s\") on line number %d";
	private static final String MSG_ERR_IO_FMT = "Problem reading input file \"%s\":\n%s";
}
