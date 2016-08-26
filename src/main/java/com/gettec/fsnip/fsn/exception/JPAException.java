package com.gettec.fsnip.fsn.exception;

public class JPAException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 609119983450392917L;

	public JPAException() {}
	
	public JPAException(String message, Object exception) {
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
