package com.booleanuk.api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public User(int id) {
        this.id = id;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}


