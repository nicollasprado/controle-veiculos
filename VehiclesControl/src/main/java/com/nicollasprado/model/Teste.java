package com.nicollasprado.model;

import com.nicollasprado.abstraction.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Teste extends Entity {
    private String name;
    private Integer num;

    public Teste(String name, Integer num) {
        super();
        this.name = name;
        this.num = num;
    }
}
