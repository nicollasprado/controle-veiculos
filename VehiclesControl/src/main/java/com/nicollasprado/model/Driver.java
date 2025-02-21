package com.nicollasprado.model;

import com.nicollasprado.abstraction.User;
import com.nicollasprado.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Driver extends User {
    private String cpf;

    public Driver(String name, String cpf) {
        super(name, UserRole.Driver);
        this.cpf = cpf;
    }
}
