package com.nicollasprado.abstraction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public abstract class Entity {
    private UUID id;

    public Entity(){
        this.id = UUID.randomUUID();
    }
}
