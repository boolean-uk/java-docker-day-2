package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tweet")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonIncludeProperties(value = "name")
    private Profile profile;

    @Column(name = "post")
    private String post;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "likes")
    private int likes;

    @Column(name = "retweets")
    private int retweets;

    public Tweet(String post, String createdAt, int likes, int retweets) {
        this.post = post;
        this.createdAt = createdAt;
        this.likes = likes;
        this.retweets = retweets;
    }
}
