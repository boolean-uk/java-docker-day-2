package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user")
    private User user;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties("post")
    private List<Comment> comments;

    public Post(int id) {
        this.id = id;
    }

    public Post(User user, String content) {
        this.user = user;
        this.content = content;
    }
}
