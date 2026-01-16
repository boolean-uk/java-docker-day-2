package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String email;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = {"user"},  allowSetters = true)
    private List<Post> posts;


    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(int id) {
        this.id = id;
    }
}
