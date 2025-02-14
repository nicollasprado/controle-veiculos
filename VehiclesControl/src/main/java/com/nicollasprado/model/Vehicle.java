package com.nicollasprado.model;

import com.nicollasprado.abstraction.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class Vehicle extends Entity {
    private String name;
    private Set<Route> routes;
    private Maintence nextMaintence;

    public Vehicle(UUID id, String name, Set<Route> routes, Maintence nextMaintence) {
        super(id);
        this.name = name;
        this.routes = routes;
        this.nextMaintence = nextMaintence;
    }
}
