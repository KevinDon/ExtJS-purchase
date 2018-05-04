package com.newaim.core.beans;

/**
 * 不支持的扩展名异常类
 */
public class OutOfSizeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OutOfSizeException(String message) {
		super(message);
	}
}