package com.nicollasprado.abstraction;

import com.nicollasprado.annotations.Column;
import com.nicollasprado.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public abstract class User {
    private UserRole userRole;

    public User(UserRole role) {
        this.userRole = role;
    }
}
