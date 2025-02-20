package com.nicollasprado.Exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
        super("Entity not found in database!");
    }
}
