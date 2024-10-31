package com.jenesano.directory.exception;

public class BusinessAlreadyValidatedException extends RuntimeException {
    public BusinessAlreadyValidatedException(Long businessId) {
        super("Negocio con id " + businessId + " ya esta validado.");
    }
}
