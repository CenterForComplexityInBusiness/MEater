package edu.umd.rhsmith.diads.tools.sentiment;

import org.plyjy.factory.PySystemObjectFactory;
import org.python.core.Py;
import org.python.core.PyString;
import org.python.core.PySystemState;

/**
 * A utility class providing a means of getting the Python-based implementation
 * of a sentiment analysis tool. (See {@link /py/SentimentAnalyzerP.py}). The
 * {@link #getSentimentAnalyzer(String, String)} method will construct and
 * return an {@link ISentimentAnalyzer} instance backed by an instance of the
 * Python tool, loading its training and feature-set data from the given
 * filenames.
 * 
 * @author dmonner
 * 
 */
public class PySentimentAnalyzer {

	private PySentimentAnalyzer() {

	}

	/*
	 * --------------------------------
	 * Python interaction
	 * --------------------------------
	 */

	/**
	 * construct and
	 * return an {@link ISentimentAnalyzer} instance backed by an instance of
	 * the
	 * Python tool, loading its training and feature-set data from the given
	 * filenames.
	 * 
	 * @param classifierFilename
	 *            the name of the serialized classifier pickle file for the
	 *            Python tool to load
	 * @param featuresFilename
	 *            the name of the serialized feature-set pickle file for the
	 *            Python tool to load
	 * @return the Python sentiment analysis tool instance
	 */
	public static ISentimentAnalyzer getSentimentAnalyzer(
			String classifierFilename, String featuresFilename) {

		String workingDir = System.getProperty("user.dir");
		String pyDir = workingDir + "/py";
		PySystemState sys = Py.getSystemState();
		sys.path.append(new PyString(workingDir));
		sys.path.append(new PyString(pyDir));
		final PySystemObjectFactory factory = new PySystemObjectFactory(sys,
				ISentimentAnalyzer.class, "SentimentAnalyzerP",
				"SentimentAnalyzerP");
		return (ISentimentAnalyzer) factory.createObject(classifierFilename,
				featuresFilename);
	}
}
