package com.journeymate.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends CustomException {
 
	private static final long serialVersionUID = 1L;

	public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}