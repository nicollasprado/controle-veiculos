package com.nicollasprado.abstraction;

import java.util.List;

public interface Persistence<T, R> {
    void save(T entity);
    <X> R findById(X id);
    List<R> findAll();
}
