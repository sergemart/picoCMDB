package ru.sergm.picocmdb.exception;


public class WrongDataException extends BaseException {

	private static final long serialVersionUID = 3L;


	public WrongDataException(String errorName, String message) {
		super(errorName, message);
	}

}
