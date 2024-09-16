package com.booleanuk.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String email;

    @OneToMany(mappedBy = "author")
    @JsonIgnoreProperties("author")
    private List<Post> posts;



    public User(String name, String email) {
        this.name = name;
        this.email = email;

    }

    public User(int id){
        this.id = id;
    }
}
