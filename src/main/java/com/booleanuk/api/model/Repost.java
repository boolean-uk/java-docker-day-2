package com.booleanuk.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "reposts")
@Getter
@Setter
@NoArgsConstructor
public class Repost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "user_id")
    private int userId;

    public Repost(int userId, Post post) {
        this.userId = userId;
        this.post = post;
    }
}
