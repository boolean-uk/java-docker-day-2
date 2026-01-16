package com.booleanuk.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "upvotes")
@Getter
@Setter
@NoArgsConstructor
public class Upvote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "post_id")
    private int postId;

    @Column(name = "user_id")
    private int userId;

    public Upvote(int postId, int userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
