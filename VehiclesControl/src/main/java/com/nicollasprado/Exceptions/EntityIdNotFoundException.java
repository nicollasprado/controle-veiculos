package com.nicollasprado.Exceptions;

public class EntityIdNotFoundException extends RuntimeException {
    public EntityIdNotFoundException() {
        super("Define @Id annotation to your class field.");
    }
}
