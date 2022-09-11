package com.crewmeister.cmcodingchallenge.exception;

import lombok.NoArgsConstructor;

public class BadRequestException extends Exception {

	private static final long serialVersionUID = -9079454849611061074L;

	public BadRequestException(final String message) {
		super(message);
	}

}