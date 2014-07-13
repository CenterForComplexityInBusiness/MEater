package edu.umd.rhsmith.diads.meater.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides static constants and utility functions for use in other classes.
 * 
 * @author dmonner
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
}
