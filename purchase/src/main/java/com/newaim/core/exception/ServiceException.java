package com.newaim.core.exception;


/**
 * Service层公用的Exception.
 */
public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = 3228265787132573746L;
	
	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
