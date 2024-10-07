package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String userName;

    @OneToMany
    @JsonIgnoreProperties("user")
    private List<User> users;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Comment> comments;

    public User(String userName){
        this.userName = userName;
    }

    public User(int id){
        this.id = id;
    }
}
