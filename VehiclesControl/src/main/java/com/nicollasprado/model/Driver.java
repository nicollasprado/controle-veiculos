package com.nicollasprado.model;

import com.nicollasprado.abstraction.User;
import com.nicollasprado.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Driver extends User {
    private String cpf;

    public Driver(UUID id, String name, String cpf) {
        super(id, name, UserRole.Driver);
        this.cpf = cpf;
    }
}
