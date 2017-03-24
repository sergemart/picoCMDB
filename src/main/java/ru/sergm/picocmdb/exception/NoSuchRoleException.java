package ru.sergm.picocmdb.exception;

public class NoSuchRoleException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message = null;


	// default constructor
	public NoSuchRoleException () {
		super();
	}


	public NoSuchRoleException (String message) {
		super(message);
		this.message = message;
	}


	public NoSuchRoleException (Throwable cause) {
		super(cause);
	}


	@Override
	public String toString() {
		return message;
	}

}
