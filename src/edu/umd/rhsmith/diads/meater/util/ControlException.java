package edu.umd.rhsmith.diads.meater.util;

public class ControlException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ControlException(String message) {
		super(message);
	}

	public ControlException(Throwable cause) {
		super(cause);
	}

	public ControlException(String message, Throwable cause) {
		super(message, cause);
	}

}
