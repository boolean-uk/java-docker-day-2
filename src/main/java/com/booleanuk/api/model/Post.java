package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"id", "email", "likes", "createdAt", "updatedAt"})
    private User user;

    @Column
    private String contents;

    @Column
    private LocalDate postedAt;

    @ManyToMany
    @JoinTable(
            name = "user_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    @JsonIgnore
    private List<User> likes;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdAt;
    @PrePersist
    private void onCreate()  {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedAt;
    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Post(
            User user,
            String contents
    )   {
        this.user = user;
        this.contents = contents;
        this.postedAt = LocalDate.now();
    }
}
