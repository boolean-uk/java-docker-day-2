package com.booleanuk.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String message;

    @Column
    private int likes;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    public Comment(int id) {
        this.id = id;
    }

    public Comment(String message, int likes) {
        this.message = message;
        this.likes = likes;
    }
}
