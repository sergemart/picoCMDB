package com.github.sergemart.picocmdb.exception;


public class DependencyExistsException extends BaseException {

	private static final long serialVersionUID = 1632417950146882072L;


	public DependencyExistsException(String errorName, String message) {
		super(errorName, message);
	}

}
