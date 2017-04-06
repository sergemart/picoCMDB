package com.github.sergemart.picocmdb.exception;


public class BaseException extends Exception {

	// Properties

	private final String errorName;
	private final String message;


	public String getErrorName() {
		return this.errorName;
	}


	public String getExceptionName() {
		return this.getClass().getCanonicalName();
	}


	// Constructors

	BaseException(String errorName, String message) {
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
