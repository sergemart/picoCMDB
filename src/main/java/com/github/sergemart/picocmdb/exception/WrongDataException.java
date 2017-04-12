package com.github.sergemart.picocmdb.exception;


public class WrongDataException extends BaseException {

	private static final long serialVersionUID = 4444678738821367588L;


	public WrongDataException(String errorName, String message) {
		super(errorName, message);
	}

}
