package com.booleanuk.library.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties(value = {"id", "username", "email"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    @JsonIncludeProperties(value = {"id", "name", "genre_category"})
    private Blog blog;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "likes_count")
    private int likesCount;

    public Post(String title, String content, int likesCount) {
        this.title = title;
        this.content = content;
        this.likesCount = likesCount;
    }
}
