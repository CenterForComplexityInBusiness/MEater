package edu.umd.rhsmith.diads.meater.core.app.components.media;

import edu.umd.rhsmith.diads.meater.core.app.MEaterConfigurationException;

public class MediaClassNotRegisteredException extends
		MEaterConfigurationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String MSG_ERR_FMT = "No registration found for media class %s";

	public MediaClassNotRegisteredException(Class<?> clazz) {
		super(String.format(MSG_ERR_FMT, clazz.getCanonicalName()));
	}
}
