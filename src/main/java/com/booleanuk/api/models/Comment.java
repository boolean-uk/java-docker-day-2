package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIncludeProperties(value = {"user", "content"})
    private Post post;

    @Column(name = "content")
    private String content;

    public Comment(int id) {
        this.id = id;
    }

    public Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
    }
}
