package com.nicollasprado.model;

import com.nicollasprado.annotations.Column;
import com.nicollasprado.annotations.Entity;
import com.nicollasprado.annotations.Id;
import com.nicollasprado.enums.IdStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Vehicle {
    @Id(strategy = IdStrategy.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private List<Route> routes;

    public Vehicle(String name) {
        this.name = name;
    }

    public Vehicle(String name, List<Route> routes) {
        this.name = name;
        this.routes = routes;
    }
}
