package ru.sergm.picocmdb.exception;


public class NoSuchObjectException extends BaseException {

	private static final long serialVersionUID = 1L;


	public NoSuchObjectException(String errorName, String message) {
		super(errorName, message);
	}

}
