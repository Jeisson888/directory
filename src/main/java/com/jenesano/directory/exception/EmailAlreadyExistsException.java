package com.jenesano.directory.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Correo " + email + " ya existe.");
    }
}
