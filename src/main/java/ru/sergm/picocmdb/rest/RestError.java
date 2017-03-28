package ru.sergm.picocmdb.rest;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;
import ru.sergm.picocmdb.config.SystemService;


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


	RestError(Exception e, String errorCode) {
		this.e = e;
		this.exceptionName = e.getClass().toString();
		this.message = e.getMessage();
		this.localizedMessage = e.getLocalizedMessage();
		//this.errorCode = this.systemService.getErrorCode(this.exceptionName);
		this.errorCode = errorCode;
	}


}

