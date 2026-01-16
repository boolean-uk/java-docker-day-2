package com.booleanuk.api.users;

import com.booleanuk.api.likes.Like;
import com.booleanuk.api.repost.Repost;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Repost> reposts;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @Column
    private String bio;

    public User(String name, String email, String password, String bio) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.bio = bio;
    }

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }

    public User(int id) {
        this.id = id;
    }
}
