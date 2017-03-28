package ru.sergm.picocmdb.exception;


public class BaseException extends Exception {

	// Properties

	protected String errorName;
	protected String message;


	public String getErrorName() {
		return this.errorName;
	}


	public String getExceptionName() {
		return this.getClass().getCanonicalName();
	}


	// Constructors

	public BaseException(String errorName, String message) {
		super(message);
		this.errorName = errorName;
		this.message = message;
	}


	// Overrides

	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + ":" + this.errorName + ":" + this.message;
	}


}
