package com.jenesano.directory.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Username" + username + "ya existe.");
    }
}
