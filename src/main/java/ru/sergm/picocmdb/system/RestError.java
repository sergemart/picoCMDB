package ru.sergm.picocmdb.system;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class RestError {

	private Exception e;
	private String exceptionName;
	private String message;
	private String localizedMessage;
	private String errorCode;


	@JsonIgnore
	public Exception getException() {
		return e;
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public String getMessage() {
		return message;
	}

	public String getLocalizedMessage() {
		return localizedMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}


	public RestError() {} // to deserialize


	public RestError(Exception e, String errorCode) {
		this.e = e;
		this.exceptionName = e.getClass().getCanonicalName();
		this.message = e.getMessage();
		this.localizedMessage = e.getLocalizedMessage();
		this.errorCode = errorCode;
	}


}

