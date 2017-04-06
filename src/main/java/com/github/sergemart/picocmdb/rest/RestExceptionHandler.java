package com.github.sergemart.picocmdb.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Locale;

import com.github.sergemart.picocmdb.exception.WrongDataException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;
import com.github.sergemart.picocmdb.system.SystemService;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;


@RestControllerAdvice
@RestController
public class RestExceptionHandler {

	@Autowired
	private SystemService systemService;


	/**
	 * Handles exceptions raised when underlying service can not find an object
	 * @return Error object with message from the exception.
	 */
	@ExceptionHandler(value = NoSuchObjectException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError handleNoSuchObjectException(NoSuchObjectException e, @RequestHeader("Accept-Language")Locale locale) {
		return this.systemService.getRestError(e, locale);
    }


	/**
	 * Handles exceptions raised when underlying service tries to create existing object
	 * @return Error object with message from the exception.
	 */
	@ExceptionHandler(value = ObjectAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestError handleObjectAlreadyExistsException(ObjectAlreadyExistsException e, @RequestHeader("Accept-Language")Locale locale) {
		return this.systemService.getRestError(e, locale);
	}


	/**
	 * Handles exceptions raised when underlying service tries to create bad-formatted object
	 * @return Error object with message from the exception.
	 */
	@ExceptionHandler(value = WrongDataException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestError handleMalformedObjectException(WrongDataException e, @RequestHeader("Accept-Language")Locale locale) {
		return this.systemService.getRestError(e, locale);
	}


	/**
	 * Handler of last resort. Handles exceptions raised when REST URL contains wrong typed subpart (eg /[string] instead of /[number]), if they are not handled in controllers.
	 * @return Error object with general message.
	 */
	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestError handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, @RequestHeader("Accept-Language")Locale locale) {
		return this.systemService.getRestError(new NoSuchObjectException("OBJECTNOTFOUND", "Object not found."), locale);
	}


	/**
	 * Handler of last resort. Handles exceptions raised when REST body contains unparseable data (anything but JSON), if they are not handled in controllers.
	 * @return Error object with general message.
	 */
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestError handleHttpMessageNotReadableException(HttpMessageNotReadableException e, @RequestHeader("Accept-Language")Locale locale) {
		return this.systemService.getRestError(new WrongDataException("OBJECTBAD", "Object missing required fields."), locale);
	}

}
