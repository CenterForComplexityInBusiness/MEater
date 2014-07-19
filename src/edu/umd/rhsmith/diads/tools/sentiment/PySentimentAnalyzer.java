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
 * @author rmachedo
 * 
 * @see ISentimentAnalyzer
 * @see PySentimentAnalyzer#getSentimentAnalyzer(String, String)
 * @see PySentimentAnalyzer.LoadFailureException
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
	 * Construct and
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
	 * 
	 * @throws NullPointerException
	 *             if either parameter is {@code null}
	 * @throws PySentimentAnalyzer.LoadFailureException
	 *             if tool instantiation fails for any
	 *             other reason.
	 */
	public static ISentimentAnalyzer getSentimentAnalyzer(
			String classifierFilename, String featuresFilename)
			throws NullPointerException,
			PySentimentAnalyzer.LoadFailureException {

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

	/**
	 * <p>
	 * An {@code Exception} representing a failed load of a Python sentiment
	 * analysis tool. Thrown by
	 * {@link PySentimentAnalyzer#getSentimentAnalyzer(String, String)} in the
	 * event of a load failure. The underlying cause of the failure is available
	 * via {@link #getCause()}.
	 * </p>
	 * <p>
	 * The parameters supplied to the failed call are available via
	 * {@link #getClassifierFilename()} and {@link #getFeaturesFilename()}
	 * respectively.
	 * </p>
	 * 
	 * @author rmachedo
	 * 
	 */
	public static class LoadFailureException extends Exception {
		private static final String MSG_ERR_LOADFAIL_FMT = "Unable to load SentimentAnalyzerP with classifier-file '%s', feature-file '%s'";

		private static final long serialVersionUID = 1L;

		private final String cFile;
		private final String fFile;

		private LoadFailureException(String cFile, String fFile, Exception cause) {
			super(String.format(MSG_ERR_LOADFAIL_FMT, cFile, fFile), cause);
			this.cFile = cFile;
			this.fFile = fFile;
		}

		/**
		 * Gets and returns the classifier filename parameter passed to the
		 * {@link PySentimentAnalyzer#getSentimentAnalyzer(String, String)} call
		 * which generated this {@code LoadFailureException}.
		 * 
		 * @return the classifier filename parameter
		 */
		public String getClassifierFilename() {
			return cFile;
		}

		/**
		 * Gets and returns the features filename parameter passed to the
		 * {@link PySentimentAnalyzer#getSentimentAnalyzer(String, String)} call
		 * which generated this {@code LoadFailureException}.
		 * 
		 * @return the features filename parameter
		 */
		public String getFeaturesFilename() {
			return fFile;
		}

	}
}
