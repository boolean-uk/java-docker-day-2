package com.booleanuk.api.library.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "nrOfLikes")
    private String nrOfLikes;

    @ManyToOne
    @JoinColumn(name ="user_id")
    @JsonIncludeProperties(value = {"name"})
    private User user;

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public Post(int id) {
        this.id = id;
    }
}
