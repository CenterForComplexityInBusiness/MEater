package edu.umd.rhsmith.diads.meater.core.app.components.media;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;

public class MediaClassNotFoundException extends MEaterConfigurationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MSG_ERR_FMT = "Unable to load requested media class '%s' (do you need to include another module?)";

	public MediaClassNotFoundException(String className) {
		super(String.format(MSG_ERR_FMT, className));
	}

}
