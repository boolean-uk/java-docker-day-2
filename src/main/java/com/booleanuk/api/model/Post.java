package com.booleanuk.api.model;

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

    @Column
    private String content;

    @Column
    private String createdAt;

    @Column
    private String updatedAt;

    @ManyToMany(mappedBy = "postsList")
    @JsonIncludeProperties(value = {"username"})
    private List<User> likes;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIncludeProperties(value = {"username", "email", "password"})
    private User user;

    public Post(String content, String createdAt, String updatedAt, User user) {
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
    }
}
