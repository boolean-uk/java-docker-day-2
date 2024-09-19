package com.booleanuk.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createAt;

    public Comment(String content) {
        this.content = content;
    }

    @PrePersist
    public void onCreate() {
        this.createAt = LocalDateTime.now();
    }
}
