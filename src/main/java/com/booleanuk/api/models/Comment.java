package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne // Many comments can be published by one user
    @JoinColumn
    @JsonIgnoreProperties({"comment"})
    private User publisher;

    @ManyToOne // Many comments can belong to one blog post
    @JoinColumn
    @JsonBackReference // To avoid recursion error
    private BlogPost blogPost;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private String text;

    public Comment(User publisher, BlogPost blogPost, String text) {
        this.publisher = publisher;
        this.blogPost = blogPost;
        this.text = text;
    }

    public Comment(String text) {
        this.text = text;
    }
}
