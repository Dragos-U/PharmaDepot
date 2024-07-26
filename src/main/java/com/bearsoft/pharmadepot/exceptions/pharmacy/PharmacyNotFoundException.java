package com.bearsoft.pharmadepot.exceptions.pharmacy;

public class PharmacyNotFoundException extends RuntimeException{

    public PharmacyNotFoundException(String message){
        super(message);
    }
}
