package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;

    public User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public User(String name, String username, List<Post> posts) {
        this.name = name;
        this.username = username;
        this.posts = posts;
    }

    public User(int id) {
        this.id = id;
    }
}
