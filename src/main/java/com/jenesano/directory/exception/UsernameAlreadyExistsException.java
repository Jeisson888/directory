package com.jenesano.directory.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Nombre de usuario " + username + " ya existe.");
    }
}
