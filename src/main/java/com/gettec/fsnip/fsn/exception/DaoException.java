package com.gettec.fsnip.fsn.exception;

public class DaoException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5345479053237370127L;

	public DaoException() {}
	
	public DaoException(String message, Object exception) {
		this.message = message;
		this.exception = exception;
	}

	String message;

	Object exception;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getException() {
		return exception;
	}

	public void setException(Object exception) {
		this.exception = exception;
	}
	
	
}
