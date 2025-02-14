package com.nicollasprado.enums;

public enum UserRole {
    Admin("admin"),
    Driver("driver");

    private final String role;

    UserRole(String role){
        this.role = role;
    }
}
