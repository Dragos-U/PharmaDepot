package com.bearsoft.pharmadepot.exceptions.pharmacy;

public class InvalidPharmacyAuthenticationException extends RuntimeException{

    public InvalidPharmacyAuthenticationException(String message) {
        super(message);
    }
}
