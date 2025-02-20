package com.nicollasprado.Exceptions;

public class TypeInferException extends RuntimeException {
    public TypeInferException() {
        super("Error trying to infer T type!");
    }
}
