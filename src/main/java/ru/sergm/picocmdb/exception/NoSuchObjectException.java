package ru.sergm.picocmdb.exception;

public class NoSuchObjectException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message = null;


	// default constructor
	public NoSuchObjectException() {
		super();
	}


	public NoSuchObjectException(String message) {
		super(message);
		this.message = message;
	}


	public NoSuchObjectException(Throwable cause) {
		super(cause);
	}


	@Override
	public String toString() {
		return message;
	}

}
