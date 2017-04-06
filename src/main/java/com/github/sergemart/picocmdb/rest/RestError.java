package com.github.sergemart.picocmdb.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.github.sergemart.picocmdb.exception.BaseException;


public class RestError {

	private Exception e;
	private String exceptionName;
	private String errorName;
	private String message;
	private String localizedMessage;
	private String errorCode;
	private String timestamp;


	@JsonIgnore
	public Exception getException() {
		return this.e;
	}

	public String getExceptionName() {
		return this.exceptionName;
	}

	public String getErrorName() {
		return this.errorName;
	}

	public String getMessage() {
		return this.message;
	}

	public String getLocalizedMessage() {
		return this.localizedMessage;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getTimestamp() {
		return this.timestamp;
	}


	public RestError() {} // default constructor required for deserialization


	public RestError(BaseException e, String errorCode, String localizedErrorMessage) {
		this.e = e;
		this.exceptionName = e.getExceptionName();
		this.errorName = e.getErrorName();
		this.message = e.getMessage();
		this.localizedMessage = localizedErrorMessage;
		this.errorCode = errorCode;
		this.timestamp = String.valueOf(System.currentTimeMillis());
	}


}

