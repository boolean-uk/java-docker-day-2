package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String createdAt;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Post> posts;

    public User(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
    }
}
