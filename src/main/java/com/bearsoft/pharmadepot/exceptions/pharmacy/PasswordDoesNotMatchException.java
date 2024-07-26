package com.bearsoft.pharmadepot.exceptions.pharmacy;

public class PasswordDoesNotMatchException extends RuntimeException{

    public PasswordDoesNotMatchException(String message) {
        super(message);
    }
}
