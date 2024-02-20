package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_follows")
public class UserFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIncludeProperties(value = {"username"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "follow_id", nullable = false)
    @JsonIncludeProperties(value = {"username"})
    private User follow;

    public UserFollow(User user, User follow) {
        this.user = user;
        this.follow = follow;
    }
}
