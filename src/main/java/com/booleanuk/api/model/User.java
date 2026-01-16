package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @JsonIgnore
    private String email;

    @Column
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "user_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")

    )
    @JsonIgnore
    private List<Post> likedPosts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

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

    public User(
            String username,
            String email,
            String password)   {
        this.username =    username;
        this.email =       email;
        this.password =    password;
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
