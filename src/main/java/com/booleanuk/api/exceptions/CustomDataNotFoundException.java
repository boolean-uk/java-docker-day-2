package com.booleanuk.api.exceptions;

public class CustomDataNotFoundException extends RuntimeException{

    public CustomDataNotFoundException() {
        super();
    }

    public CustomDataNotFoundException(String message) {
        super(message);
    }
}
