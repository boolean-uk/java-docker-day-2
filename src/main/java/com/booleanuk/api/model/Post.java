package com.booleanuk.api.model;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(nullable = false)
    private String content;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public Post() {
    }
    public Post(int userId, String content) {
        this.userId = userId;
        this.content = content;
    }
}

