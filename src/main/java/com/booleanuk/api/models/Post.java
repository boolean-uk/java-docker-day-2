package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column
    private String timeAndDate;

    @Column
    private String message;

    @Column
    private int likes;

    @ManyToOne
    @JsonIgnoreProperties(value ={"users", "posts", "followers"})
    @JoinColumn(name="userId")
    private User user;

    public Post(String timeAndDate, String message){
        this.timeAndDate=timeAndDate;
        this.message=message;
        this.likes=0;
    }







}
