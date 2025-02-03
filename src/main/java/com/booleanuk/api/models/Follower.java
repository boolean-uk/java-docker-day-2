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
@Table(name = "followers")
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User following;

    @Column
    private LocalDateTime createdAt;

    public Follower(User follower, User following){
        this.follower = follower;
        this.following = following;
        createdAt = LocalDateTime.now();
    }

    public Follower(){
        createdAt = LocalDateTime.now();
    }
}
