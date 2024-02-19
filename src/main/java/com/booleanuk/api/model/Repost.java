package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "reposts")
public class Repost {

    @EmbeddedId
    private UserPostKey id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIncludeProperties(value = {"username"})
    private User user;

    @ManyToOne
    @MapsId("post_id")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public Repost(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
