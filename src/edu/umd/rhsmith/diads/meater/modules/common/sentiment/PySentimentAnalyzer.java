package edu.umd.rhsmith.diads.meater.modules.common.sentiment;

import org.plyjy.factory.PySystemObjectFactory;
import org.python.core.Py;
import org.python.core.PyString;
import org.python.core.PySystemState;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.ComponentManager;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;
import edu.umd.rhsmith.diads.meater.util.ControlException;

public class PySentimentAnalyzer extends Component implements
		MediaProcessor<SentimentAnalyzable> {

	private ISentimentAnalyzer ptool;
	private final String classifierFilename;
	private final String featuresFilename;

	public PySentimentAnalyzer(PySentimentAnalyzerInitializer init,
			ComponentManager mgr) throws MEaterConfigurationException {
		super(init, mgr);

		this.classifierFilename = init.getClassifierFilename();
		this.featuresFilename = init.getFeaturesFilename();

		this.registerMediaProcessor(this);
	}

	@Override
	protected void doStartupRoutine() throws ControlException {
	}

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
		this.logInfo(MSG_LOADING_TOOL);
		this.ptool = getPythonTool(this.classifierFilename,
				this.featuresFilename);
		this.logInfo(MSG_LOADED_TOOL);
	}

	@Override
	protected void doShutdownRoutine() {
	}

	@Override
	public Class<SentimentAnalyzable> getMediaClass() {
		return SentimentAnalyzable.class;
	}

	@Override
	public String getProcessorName() {
		return "";
	}

	@Override
	public boolean processMedia(SentimentAnalyzable media) {
		this.ptool.process(media.getSentimentAnalysisText());
		return true;
	}

	/*
	 * --------------------------------
	 * Python interaction
	 * --------------------------------
	 */

	public static ISentimentAnalyzer getPythonTool(String classifierFilename,
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

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_LOADED_TOOL = "Sentiment analysis tool loaded";
	private static final String MSG_LOADING_TOOL = "Loading sentiment analysis tool";

}
