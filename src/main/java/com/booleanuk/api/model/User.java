package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "byUser")
    @JsonIgnoreProperties("byUser")
    private List<Post> posts;

    @ManyToMany
    @JsonIncludeProperties("id")
    private List<Post> reposts;

    @ManyToMany
    @JsonIncludeProperties(value = {"id", "username"})
    private List<User> friends;

    public void addPost(Post p) {
        this.posts.add(p);
    }

    public void addRepost(Post post) {
        this.reposts.add(post);
    }

    public void addFriend(User user) {
        this.friends.add(user);
    }

}
