package com.nicollasprado.Exceptions;

public class InvalidQueryType extends RuntimeException {
    public InvalidQueryType(String cause) {
        super("Invalid query type: " + cause);
    }
}
