package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "upvotes")
public class Upvote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties({"username"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")

    private Post post;

    public Upvote(User user, Post post){
        this.user = user;
        this.post = post;
    }

    public Upvote(int id){
        this.id = id;
    }
}
