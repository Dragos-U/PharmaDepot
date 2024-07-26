package com.bearsoft.pharmadepot.exceptions.pharmacy;

public class PharmacyAlreadyExistsException extends RuntimeException{

    public PharmacyAlreadyExistsException(String message) {
        super(message);
    }
}
