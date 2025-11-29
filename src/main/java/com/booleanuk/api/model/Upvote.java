package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "upvotes")
public class Upvote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"post", "upvotes"})
    @JsonIncludeProperties(value = {"username"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties(value = {"user"})
    @JsonIncludeProperties(value = {"message"})
    private Post post;


    public Upvote(User user, Post post){
        this.post = post;
        this.user = user;
    }
}
