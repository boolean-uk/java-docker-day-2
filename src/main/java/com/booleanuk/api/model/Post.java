package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message")
    private String message;

    @Column(name = "createdAt")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"posts"})
    @JsonIncludeProperties(value = {"username"})
    private User user;


    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties("posts")
    private List<Upvote> upvotes;

    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties("posts")
    private List<Repost> reposts;


    public Post(String message, User user) {
        this.message = message;
        this.createdAt = new Date();
        this.user = user;
    }
}
