package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 25)
    private String username;
    @Column(length = 50)
    private String email;
    @Column(length = 120)
    private String password;

    // we could add bio, profile picture etc.

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Post> posts;

    @Override
    public boolean haveNullFields() {
        return username == null || email == null || password == null;
    }

    @Override
    public void copyOverData(Model model) {
        User _other = (User) model;

        username = _other.username;
        email = _other.email;
        password = _other.password;

        // usually you don't actually want to replace all of the posts
        //posts = _other.posts;
    }
}
