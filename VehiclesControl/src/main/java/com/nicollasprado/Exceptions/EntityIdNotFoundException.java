package com.nicollasprado.Exceptions;

public class EntityIdNotFoundException extends RuntimeException {
    public EntityIdNotFoundException(String entityName) {
        super(entityName + " class don't have @Id annotation on any field.");
    }
}
