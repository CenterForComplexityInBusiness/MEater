package edu.umd.rhsmith.diads.meater.modules.common.sentiment;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.tools.sentiment.ISentimentAnalyzer;
import edu.umd.rhsmith.diads.tools.sentiment.PySentimentAnalyzer;

public class PySentimentTool extends Component implements
		MediaProcessor<SentimentAnalyzable> {

	private ISentimentAnalyzer ptool;
	private final String classifierFilename;
	private final String featuresFilename;

	public PySentimentTool(PySentimentToolInitializer init) throws MEaterConfigurationException {
		super(init);

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
		this.ptool = PySentimentAnalyzer.getSentimentAnalyzer(this.classifierFilename,
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
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_LOADED_TOOL = "Sentiment analysis tool loaded";
	private static final String MSG_LOADING_TOOL = "Loading sentiment analysis tool";

}
