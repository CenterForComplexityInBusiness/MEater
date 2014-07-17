package edu.umd.rhsmith.diads.meater.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides static constants and utility functions for use in other classes.
 * 
 * @author dmonner
 * @author rmachedo
 */
public class Util {
	private Util() {
	}

	/**
	 * Conversion constant for bytes to megabytes
	 */
	public static final int MB = 1 << 20;

	/**
	 * Gets the hostname of the machine running the code, foregoing the need for
	 * exception handling in-line when this information is needed.
	 * 
	 * @return The hostname of the machine
	 */
	public static String getHost() {
		String host = "unknown";
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException ex) {
			ex.printStackTrace();
		}
		return host;
	}

	/**
	 * Get the amount of memory used by the current JVM as a fraction of the
	 * maximum amount of memory it is allowed to use (via the <code>-Xmx</code>
	 * parameter)
	 * 
	 * @return Memory utilization as a <code>float</code> between
	 *         <code>0.0</code> and <code>1.0</code>.
	 */
	public static float getMemoryUtilizationFraction() {
		final Runtime rt = Runtime.getRuntime();
		final float usedMemory = rt.totalMemory() - rt.freeMemory();
		return usedMemory / rt.maxMemory();
	}

	/**
	 * Makes a list into a pretty string by putting each element on its own
	 * indented line and enclosing the whole thing in brackets.
	 * 
	 * @param <E>
	 *            The type of the list
	 * @param list
	 *            The list to convert
	 * @return A string representing the list
	 */
	public static <E> String prettyPrintList(final List<E> list) {
		final StringBuffer sb = new StringBuffer();
		sb.append("[\n");
		for (final E item : list) {
			sb.append("\t");
			sb.append(item.toString());
			sb.append("\n");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Converts an object with a stack trace into a readable message which can
	 * be used for logging.
	 * 
	 * @param t
	 *            The object to convert
	 * @return A readable error message
	 */
	public static String traceMessage(final Throwable t) {
		final StringBuffer sb = new StringBuffer();
		sb.append(t.toString());
		sb.append("\n");
		for (final StackTraceElement elem : t.getStackTrace()) {
			sb.append("\t");
			sb.append(elem.toString());
			sb.append("\n");
		}
		if (t.getCause() != null) {
			sb.append("Caused by:\n");
			sb.append(traceMessage(t.getCause()));
		}
		return sb.toString();
	}

	/**
	 * @param s
	 * @return the input string, with double-quotes removed on either end.
	 */
	public static String unquoteString(String s) {
		while (s.startsWith("\"")) {
			s = s.substring(1);
		}

		while (s.endsWith("\"")) {
			s = s.substring(0, s.length() - 1);
		}

		return s;
	}

	public static String indentLines(String text, int levels, String pattern) {
		String[] lines = text.replace("\r", "").split("\n");

		StringBuilder indent = new StringBuilder();
		for (int x = 0; x < levels; x++) {
			indent.append(pattern);
		}

		int idx = 0;
		StringBuilder total = new StringBuilder();
		for (String line : lines) {
			++idx;
			total.append(indent);
			total.append(line);
			if (idx != lines.length) {
				total.append('\n');
			}
		}

		return total.toString();
	}

	/**
	 * Splits the string <code>s</code> at every delimiter <code>delim</code>,
	 * unless that delimiter
	 * is between two matched occurrences of the <code>quote</code> character.
	 * 
	 * @param s
	 * @param delim
	 * @param quote
	 * @return
	 */
	public static String[] splitUnlessQuoted(final String s,
			final String delim, final String quote) {
		final List<String> tokens = new LinkedList<String>();

		boolean quoted = false;
		int start = 0;

		for (int i = 0; i < s.length(); i++) {
			if (s.startsWith(delim, i)) {
				if (!quoted) {
					if (start < i) {
						tokens.add(s.substring(start, i));
						start = i + delim.length();
					} else {
						start++;
					}
				}
			} else if (s.startsWith(quote, i)) {
				quoted = !quoted;
			}
		}

		if (start < s.length()) {
			tokens.add(s.substring(start, s.length()));
		}

		return tokens.toArray(new String[tokens.size()]);
	}

	public static String joinStrings(Iterable<?> list, String delimeter) {
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for (Object o : list) {
			if (!first) {
				b.append(delimeter);
			}
			b.append(o.toString());
			first = false;
		}
		return b.toString();
	}

	/**
	 * The number of milliseconds before an HTTP connection is considered to
	 * have timed out.
	 */
	private static final int CONN_TIMEOUT_MS = 10 * 1000;

	/**
	 * A utility method that takes any URL and follows all redirects recursively
	 * until the true target
	 * is reached. Useful for finding the true destination of URL-shortened
	 * links.
	 * 
	 * @param urlStr
	 *            A URL, possibly shortened.
	 * @return The true destination URL.
	 */
	public static String expandUrl(final String urlStr)
	{
		URLConnection conn = null;

		try
		{
			// to expand the URL, just open a connection and follow it!
			final URL inputURL = new URL(urlStr);
			conn = inputURL.openConnection();
			conn.setConnectTimeout(CONN_TIMEOUT_MS);
			conn.setReadTimeout(CONN_TIMEOUT_MS);

			// this is necessary to update the header fields and can lead to
			// StringIndexOutOfBoundsException if bad page returned
			conn.getHeaderFields();

			return conn.getURL().toString();
		}
		catch(final SocketTimeoutException ex)
		{
			// log.info("Socket timeout when trying to access " + urlStr);
		}
		catch(final NullPointerException ex)
		{
			// log.info("Null URL");
		}
		catch(final StringIndexOutOfBoundsException ex)
		{
			/*
			String badURL = urlStr;

			try
			{
				badURL = conn.getURL().toString();
			}
			catch(final Exception ex2)
			{
				// log.severe(Util.traceMessage(ex2));
			}

			// log.info("Header issue with URL: " + badURL);
			 */
		}
		catch(final IllegalArgumentException ex)
		{
			if(ex.getMessage().contains("host = null"))
			{
				// log.info("Null hostname in URL.");
			}
			else
			{
				// log.info("URL Error: " + ex.getMessage());
			}
		}
		catch(final MalformedURLException ex)
		{
			// log.info("Invalid URL: " + urlStr);
		}
		catch(final IOException ex)
		{
			// log.info("Cannot connect to URL: " + urlStr);
		}
		

		return urlStr;
	}
}
