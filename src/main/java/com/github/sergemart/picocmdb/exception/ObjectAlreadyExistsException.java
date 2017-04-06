package com.github.sergemart.picocmdb.exception;


public class ObjectAlreadyExistsException extends BaseException {

	private static final long serialVersionUID = 2L;


	public ObjectAlreadyExistsException(String errorName, String message) {
		super(errorName, message);
	}

}
