package com.booleanuk.api.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;

    @OneToMany
    List<Post> posts;

    @ManyToMany
    Set<User> following;

    // the way this is implemented, it's possible that following-follower is a 1-way relationship, which should never happen.
    // but i'll leave it like this for now; it works as long as nothing unexpected happens
    @ManyToMany
    Set<User> followers;

    public User(String username, List<Post> posts, Set<User> following, Set<User> followers) {
        this.username = username;
        this.posts = posts;
        this.following = following;
        this.followers = followers;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }
}
