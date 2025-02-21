package com.nicollasprado.abstraction;

import com.nicollasprado.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public abstract class User extends Entity {
    private String username;
    private UserRole userRole;

    public User(String username, UserRole role) {
        super();
        this.username = username;
        this.userRole = role;
    }
}
