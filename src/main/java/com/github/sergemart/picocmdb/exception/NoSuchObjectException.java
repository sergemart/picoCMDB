package com.github.sergemart.picocmdb.exception;


public class NoSuchObjectException extends BaseException {

	private static final long serialVersionUID = 8712824828892548667L;


	public NoSuchObjectException(String errorName, String message) {
		super(errorName, message);
	}

}
