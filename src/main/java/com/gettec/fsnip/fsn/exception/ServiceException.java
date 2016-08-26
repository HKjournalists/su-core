package com.gettec.fsnip.fsn.exception;

/**
 * service 层的异常信息
 * 
 * @author Administrator
 * 
 */
public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3726430705530309153L;

	public ServiceException() {
	};

	public ServiceException(String message, Object exception) {
		this.message = message;
		this.exception = exception;
	};

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
