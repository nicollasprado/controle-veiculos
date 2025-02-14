package com.nicollasprado.abstraction;

import com.nicollasprado.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class User extends Entity {
    private String name;
    private UserRole userRole;

    public User(UUID id, String name, UserRole role) {
        super(id);
        this.name = name;
        this.userRole = role;
    }
}
