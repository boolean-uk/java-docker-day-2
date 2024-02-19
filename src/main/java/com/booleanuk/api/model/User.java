package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column
    private String name;

    @Column
    private String createdAt;

    @Column
    private String updatedAt;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    private List<Post> posts;


    public User(String name, String createdAt, String updatedAt) {
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(int id) {
        this.id = id;
    }
}
