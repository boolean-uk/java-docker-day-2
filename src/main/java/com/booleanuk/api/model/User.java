package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String displayName;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;

    /*@OneToMany(mappedBy = "User")
    private List<Post> likes;
    */

    @CreationTimestamp
    private LocalDateTime createdAt;
    @PrePersist
    private void onCreate()  {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public User(
            String username,
            String displayName,
            String email)   {
        this.username =    username;
        this.displayName = displayName;
        this.email =       email;
    }

    public User(int id)   {
        this.id = id;
    }

    public void addPost(Post post)  {
        this.posts.add(post);
    }

    public void removePost(Post post)   {
        this.posts.remove(post);
    }
}
