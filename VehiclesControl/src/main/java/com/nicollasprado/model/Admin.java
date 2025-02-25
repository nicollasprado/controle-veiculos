package com.nicollasprado.model;

import com.nicollasprado.abstraction.User;
import com.nicollasprado.annotations.Column;
import com.nicollasprado.annotations.Entity;
import com.nicollasprado.annotations.Id;
import com.nicollasprado.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "admin")
public class Admin extends User {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column
    private String password;
    @Column(name = "username")
    private String username;

    public Admin(String name, String password) {
        super(UserRole.Admin);
        this.username = name;
        this.password = password;
    }
}
