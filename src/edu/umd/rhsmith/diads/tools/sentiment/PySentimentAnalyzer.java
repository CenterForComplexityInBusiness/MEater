package edu.umd.rhsmith.diads.tools.sentiment;

import org.plyjy.factory.PySystemObjectFactory;
import org.python.core.Py;
import org.python.core.PyString;
import org.python.core.PySystemState;

public class PySentimentAnalyzer {
	
	/*
	 * --------------------------------
	 * Python interaction
	 * --------------------------------
	 */

	public static ISentimentAnalyzer getSentimentAnalyzer(String classifierFilename,
			String featuresFilename) {
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
