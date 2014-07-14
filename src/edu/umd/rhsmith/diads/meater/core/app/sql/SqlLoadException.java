package edu.umd.rhsmith.diads.meater.core.app.sql;

import edu.umd.rhsmith.diads.meater.core.app.MEaterException;

public class SqlLoadException extends MEaterException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SqlLoadException(String message) {
		super(message);
	}

	public SqlLoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public SqlLoadException(Throwable cause) {
		super(cause);
	}
}
