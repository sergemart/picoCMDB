package ru.sergm.picocmdb.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ru.sergm.picocmdb.exception.NoSuchObjectException;


@RestControllerAdvice
@RestController
public class RestExceptionHandler {

    @ExceptionHandler(value = NoSuchObjectException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError handleNoSuchObjectException(NoSuchObjectException e) {
        return new RestError(e);
    }

}
