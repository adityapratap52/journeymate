package com.journeymate.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends CustomException {

	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}