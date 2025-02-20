package com.nicollasprado.model;

import com.nicollasprado.abstraction.User;
import com.nicollasprado.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Admin extends User {
    private String password;

    public Admin(String name, String password) {
        super(name, UserRole.Admin);
        this.password = password;
    }
}
