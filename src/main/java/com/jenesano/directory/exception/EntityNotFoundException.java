package com.jenesano.directory.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, Long id) {
        super(entity + " con id " + id + " no encontrado.");
    }
}
