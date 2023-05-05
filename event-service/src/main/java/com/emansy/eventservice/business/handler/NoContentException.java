package com.emansy.eventservice.business.handler;

public class NoContentException extends RuntimeException{
    public NoContentException() {
        super("No Content Found on server");
    }

    public NoContentException(String message) {
        super(message);
    }
}
