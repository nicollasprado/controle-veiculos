package com.nicollasprado.model;

import com.nicollasprado.abstraction.Entity;
import com.nicollasprado.db.annotations.Column;
import com.nicollasprado.db.annotations.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Teste extends Entity {
    @Id
    @Column
    private String name;
    @Column(name = "num")
    private Integer num;

    public Teste(String name, Integer num) {
        super();
        this.name = name;
        this.num = num;
    }
}
