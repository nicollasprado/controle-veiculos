package com.nicollasprado.model;

import com.nicollasprado.annotations.Column;
import com.nicollasprado.annotations.Entity;
import com.nicollasprado.annotations.Id;
import com.nicollasprado.enums.IdStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Driver {
    @Id(strategy = IdStrategy.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cpf;

    public Driver(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }
}
