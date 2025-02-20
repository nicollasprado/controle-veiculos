package com.nicollasprado.db;

import com.nicollasprado.abstraction.Entity;
import com.nicollasprado.abstraction.Persistence;

import java.util.List;

public class SqlRepository<T extends Entity> implements Persistence<T> {
    private final Class<T> type;

    public List<String> getById(String id){
        Query<T> query = new Query<>();

        return query.StandartQuery("SELECT * FROM teste WHERE name = '" + id + "'");
    }

    public SqlRepository(Class<T> type){
        this.type = type;
    }
}
