package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Post(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public Post(String title, String message, User user) {
        this.title = title;
        this.message = message;
        this.user = user;
    }

    public Post(int id) {
        this.id = id;
    }
}
