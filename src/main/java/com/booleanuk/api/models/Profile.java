package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne()
    @JoinColumn(name = "account_id")
    @JsonIncludeProperties(value = "user_name")
    private Account account;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "following")
    private int following;

    @Column(name = "followers")
    private int followers;

    @OneToMany(mappedBy = "profile")
    @JsonIgnoreProperties("profile")
    private List<Tweet> tweets;

    public Profile(Account account, String name, String description, int following, int followers) {
        this.account = account;
        this.name = name;
        this.description = description;
        this.following = following;
        this.followers = followers;
    }
}
