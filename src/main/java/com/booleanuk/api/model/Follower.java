package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "followers")
@JsonIncludeProperties({"id", "isFollowing", "isFollowed"})
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @JsonIgnoreProperties({"password", "roles", "posts"})
    private User isFollowing;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    @JsonIgnoreProperties({"password", "roles", "posts"})
    private User isFollowed;

    public Follower(User isFollowing, User isFollowed){
        this.isFollowing = isFollowing;
        this.isFollowed = isFollowed;
    }

    public Follower(int id){
        this.id = id;
    }

}
