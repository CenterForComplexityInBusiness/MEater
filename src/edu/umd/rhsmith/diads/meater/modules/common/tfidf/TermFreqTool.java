package edu.umd.rhsmith.diads.meater.modules.common.tfidf;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;
import edu.umd.rhsmith.diads.meater.core.app.components.Component;
import edu.umd.rhsmith.diads.meater.core.app.components.media.MediaProcessor;
import edu.umd.rhsmith.diads.meater.util.ControlException;
import edu.umd.rhsmith.diads.tools.tfidf.ITermExtractor;
import edu.umd.rhsmith.diads.tools.tfidf.SimpleTermExtractor;

public class TermFreqTool extends Component implements
		MediaProcessor<TermExtractable> {

	private ITermExtractor etool;

	public TermFreqTool(TermFreqToolInitializer init)
			throws MEaterConfigurationException {
		super(init);
		this.registerMediaProcessor(this);
	}

	@Override
	protected void doStartupRoutine() throws ControlException {
	}

	@Override
	protected void doInitRoutine() throws MEaterConfigurationException {
		this.logInfo(MSG_LOADING_TOOL);
		this.etool = new SimpleTermExtractor();
		this.logInfo(MSG_LOADED_TOOL);
	}

	@Override
	protected void doShutdownRoutine() {
	}

	@Override
	public Class<TermExtractable> getMediaClass() {
		return TermExtractable.class;
	}

	@Override
	public String getProcessorName() {
		return "";
	}

	@Override
	public boolean processMedia(TermExtractable media) {
		this.etool.process(media.getTermExtractionText());
		return true;
	}

	/*
	 * --------------------------------
	 * Messages
	 * --------------------------------
	 */

	private static final String MSG_LOADED_TOOL = "Term extraction tool loaded";
	private static final String MSG_LOADING_TOOL = "Loading term extraction tool";

}
