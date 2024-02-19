package com.booleanuk.api.models;

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

    //private User follower;

    //private User follows;

    public Follow(int id) {
        this.id = id;
    }

    //public Follow(User follower, User follows) {
    //    this.follower = follower;
    //    this.follows = follows;
    //}
}
