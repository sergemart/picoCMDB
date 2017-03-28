package ru.sergm.picocmdb.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ru.sergm.picocmdb.config.SystemService;
import ru.sergm.picocmdb.exception.NoSuchObjectException;


@RestControllerAdvice
@RestController
public class RestExceptionHandler {

	@Autowired
	private SystemService systemService;


	@ExceptionHandler(value = NoSuchObjectException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError handleNoSuchObjectException(NoSuchObjectException e) {
        String errorCode = systemService.getErrorCode(e.getClass().getCanonicalName());
		return new RestError(e, errorCode);
    }

}
