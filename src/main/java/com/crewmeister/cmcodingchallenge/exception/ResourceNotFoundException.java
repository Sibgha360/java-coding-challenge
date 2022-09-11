package com.crewmeister.cmcodingchallenge.exception;

public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = -9079454849611061074L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}