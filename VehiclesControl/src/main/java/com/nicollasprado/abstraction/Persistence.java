package com.nicollasprado.abstraction;

public interface Persistence {
    Object save();
    Object findById();
    Object findAll();
}
