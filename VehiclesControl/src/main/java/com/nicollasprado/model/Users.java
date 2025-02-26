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
public class Users {
    @Id(strategy = IdStrategy.UUID)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
