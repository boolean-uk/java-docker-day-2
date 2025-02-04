package com.booleanuk.api.tweet;


import com.booleanuk.api.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tweets")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "message", nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // FK column in "tweets" table
    @JsonIgnoreProperties({"tweets"})
    private User user;

    public Tweet(String message, User user){
        this.message = message;
        this.user = user;
    }

    public Tweet(int id){
        this.id = id;
    }
}
