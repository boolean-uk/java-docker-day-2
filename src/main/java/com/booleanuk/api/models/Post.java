package com.booleanuk.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String content;
    @Column
    private String postedAt;
    @Column
    private String updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post(String content) {
        this.content = content;
    }
}
