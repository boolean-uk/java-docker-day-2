package com.booleanuk.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userName;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = {"user", "userUpvotes"})
    private List<Post> posts;

    public User(String userName) {
        this.userName = userName;
    }

    public User(int id) {
        this.id = id;
    }
}
