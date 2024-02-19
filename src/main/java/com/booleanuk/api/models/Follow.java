package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @JsonIncludeProperties(value = {"username"})
    private User follower;

    @ManyToOne
    @JoinColumn(name = "follows_id")
    @JsonIncludeProperties(value = {"username"})
    private User follows;

    public Follow(int id) {
        this.id = id;
    }

    public Follow(User follower, User follows) {
        this.follower = follower;
        this.follows = follows;
    }
}
