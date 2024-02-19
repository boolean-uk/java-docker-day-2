package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;

    @Column
    private String email;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserFollow> userFollows;

    @OneToMany(mappedBy = "follow")
    @JsonIgnore
    private List<UserFollow> followsUser;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Like> likes;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Repost> reposts;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
