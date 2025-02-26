package com.nicollasprado.abstraction;

import java.util.List;
import java.util.Optional;

public interface Persistence<T, R> {
    void save(T entity);
    <X> Optional<R> findById(X id);
    Optional<List<R>> findAll();
}
