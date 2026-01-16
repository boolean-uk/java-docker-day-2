package com.booleanuk.api.repost;

import com.booleanuk.api.posts.Post;
import com.booleanuk.api.users.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reposts")
public class Repost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime repostedAt;

    @PrePersist
    public void prePersist() {
        repostedAt = OffsetDateTime.now();
    }

    public Repost(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
