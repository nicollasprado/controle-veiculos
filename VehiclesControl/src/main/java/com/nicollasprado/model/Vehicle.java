package com.nicollasprado.model;

import com.nicollasprado.abstraction.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Vehicle extends Entity {
    private String name;
    private Set<Route> routes;
    private Maintence nextMaintence;

    public Vehicle(String name, Set<Route> routes, Maintence nextMaintence) {
        this.name = name;
        this.routes = routes;
        this.nextMaintence = nextMaintence;
    }
}
