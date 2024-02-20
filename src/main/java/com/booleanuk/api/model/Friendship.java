package com.booleanuk.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "friendships")
@Getter
@Setter
@NoArgsConstructor
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "friend_id")
    private int friendId;

    public Friendship(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}
