package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "upvotes")
public class Upvote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

    @Column
    private LocalDateTime createdAt;

    public Upvote(User user, Post post){
        this.user = user;
        this.post = post;
        createdAt = LocalDateTime.now();
    }

    public Upvote(){
        createdAt = LocalDateTime.now();
    }

    public Upvote(int id){
        this.id = id;
        createdAt = LocalDateTime.now();
    }
}
