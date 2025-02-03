package com.booleanuk.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne @JoinColumn
    private User user;

    @Column
    private String body;

    @ManyToOne
    private Post originalPost;

    @Column
    private LocalDateTime timestamp;

    @ManyToMany
    private List<User> upvotes;

    public Post(User user, String body, Post originalPost) {
        this.user = user;
        this.body = body;
        this.originalPost = originalPost;
    }

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<User> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(List<User> upvotes) {
        this.upvotes = upvotes;
    }

    public Post getOriginalPost() {
        return originalPost;
    }

    public void setOriginalPost(Post originalPost) {
        this.originalPost = originalPost;
    }
}
