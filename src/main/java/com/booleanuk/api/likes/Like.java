package com.booleanuk.api.likes;

import com.booleanuk.api.posts.Post;
import com.booleanuk.api.users.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    Post post;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime createdAt;


    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }

    public Like(int id) {
        this.id = id;
    }

}

