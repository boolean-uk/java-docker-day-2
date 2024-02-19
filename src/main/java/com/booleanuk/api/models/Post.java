package com.booleanuk.api.models;

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

    @Column
    private String description;

    @Column(name = "is_liked")
    private boolean like;

    @Column
    private boolean repost;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false )
    @JsonIncludeProperties(value = {"id"})
    private User user;

    public Post(String description, boolean like, boolean repost, String comment) {
        this.description = description;
        this.like = like;
        this.repost = repost;
        this.comment = comment;
    }
}
