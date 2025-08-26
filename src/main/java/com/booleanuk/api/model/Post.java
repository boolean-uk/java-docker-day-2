package com.booleanuk.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    //@JsonIncludeProperties(value = {})
    @JsonIgnoreProperties("posts")
    private User user;

    @ManyToMany
    @JoinColumn(name = "upvotes")
    @JsonIgnoreProperties("posts")
    private List<User> userUpvotes;


    public Post(String content) {
        this.content = content;
    }

    public Post(int id) {
        this.id = id;
    }
}
