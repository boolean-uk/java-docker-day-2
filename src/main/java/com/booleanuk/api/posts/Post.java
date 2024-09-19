package com.booleanuk.api.posts;

import com.booleanuk.api.likes.Like;
import com.booleanuk.api.repost.Repost;
import com.booleanuk.api.users.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("post")
    private List<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Repost> reposts;

    @Column
    private String content;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    @Column
    private int likesCount = 0;

    @Column
    private int repostsCount;

    public Post(User user, String content) {
        this.user = user;
        this.content = content;
    }

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }

}
