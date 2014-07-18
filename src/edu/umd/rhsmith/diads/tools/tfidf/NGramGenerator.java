package edu.umd.rhsmith.diads.tools.tfidf;

import java.util.LinkedList;
import java.util.List;

public class NGramGenerator extends DefaultTermFilter {

	private final int maxN;

	public NGramGenerator(int n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}

		this.maxN = n;
	}

	@Override
	public List<String> filterTerms(List<String> terms) {
		if (this.getPreFilter() != null) {
			terms = this.getPreFilter().filterTerms(terms);
		}

		List<String> nGrams = generateNGrams(terms, maxN, this.getCleaner());

		return nGrams;
	}

	public static List<String> generateNGrams(List<String> terms, int maxN,
			TermCleaner cleaner) {
		String[] t = new String[terms.size()];
		terms.toArray(t);

		List<String> nGrams = new LinkedList<String>();

		for (int s = 0; s < t.length; s++) {
			StringBuilder nGram = new StringBuilder();
			for (int x = s; x < s + maxN && x < t.length; x++) {
				String term = t[x];
				if (cleaner != null) {
					term = cleaner.clean(term);
					if (term == null) {
						continue;
					}
				}

				nGram.append(term);
				nGrams.add(nGram.toString());
				nGram.append(' ');
			}
		}

		return nGrams;
	}

	public static List<String> generateNGrams(List<String> terms, int maxN) {
		return generateNGrams(terms, maxN, null);
	}
}
