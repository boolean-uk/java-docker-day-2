package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "posts")
@JsonIgnoreProperties({"upvotes"})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @Column
    private String content;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Upvote> upvotes;

    @Column
    private LocalDateTime createdAt;

    public Post(User user, String content){
        this.user = user;
        this.content = content;
        createdAt = LocalDateTime.now();
    }

    public Post(){
        createdAt = LocalDateTime.now();
    }
}
