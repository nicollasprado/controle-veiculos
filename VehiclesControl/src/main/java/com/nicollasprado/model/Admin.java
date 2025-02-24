package com.nicollasprado.model;

import com.nicollasprado.abstraction.User;
import com.nicollasprado.db.annotations.Entity;
import com.nicollasprado.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "admin")
public class Admin extends User {
    private String password;

    public Admin(String name, String password) {
        super(name, UserRole.Admin);
        this.password = password;
    }
}
