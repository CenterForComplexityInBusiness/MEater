package edu.umd.rhsmith.diads.tools.tfidf;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class StopWordsCleaner implements TermCleaner {

	private final HashSet<String> stopWords;

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

	public static Collection<String> stopWordsFromFile(String filename)
			throws IOException {
		return stopWordsFromFile(new File(filename));
	}

	public static Collection<String> stopWordsFromFile(File file)
			throws IOException {
		Collection<String> words = new LinkedList<String>();
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
