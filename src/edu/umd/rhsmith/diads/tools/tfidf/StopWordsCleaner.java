package edu.umd.rhsmith.diads.tools.tfidf;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * An implementation of {@link TermCleaner} which discards terms that appear in
 * a given collection of stop-words. Also provides convenience methods for
 * loading such a list from an input file.
 * 
 * @author rmachedo
 * 
 */
public class StopWordsCleaner implements TermCleaner {

	private final HashSet<String> stopWords;

	/**
	 * Creates a new <code>StopWordsCleaner</code> using the given
	 * {@link Collection} of term strings as its filter set. Note that the
	 * filter set is copied from the given collection, so altering the
	 * collection after an instance is constructed with this method will not
	 * affect the instance's filtering.
	 * 
	 * @param stopWords
	 *            the {@link Collection} of term strings as the filter set of
	 *            this <code>StopWordsCleaner</code>
	 */
	public StopWordsCleaner(Collection<String> stopWords) {
		this.stopWords = new HashSet<String>(stopWords);
	}

	@Override
	public String clean(String analysisText) {
		if (stopWords.contains(analysisText)) {
			return null;
		} else {
			return analysisText;
		}
	}

	/**
	 * 
	 * @param filename
	 *            the name of the file to load stop-words from
	 * @return the resulting {@link Set} of stop-words
	 * @throws IOException
	 *             if an i/o exception occurs while loading the file
	 */
	public static Set<String> stopWordsFromFile(String filename)
			throws IOException {
		return stopWordsFromFile(new File(filename));
	}

	/**
	 * 
	 * @param filename
	 *            the file to load stop-words from
	 * @return the resulting {@link Set} of stop-words
	 * @throws IOException
	 *             if an i/o exception occurs while loading the file
	 */
	public static Set<String> stopWordsFromFile(File file) throws IOException {
		Set<String> words = new HashSet<String>();
		Scanner s = new Scanner(file);
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (line.startsWith("#")) {
				continue;
			}
			line = line.trim();
			words.add(line);
		}
		s.close();
		return words;
	}
}
