package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd mm:ss")
    private LocalDateTime postedOn;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd mm:ss")
    private LocalDateTime lastUpdatedOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"posts", "reposts", "friends"})
    private User byUser;

    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties("post")
    private List<Reaction> reactions;

    public Post(int id) {
        this.id = id;
    }

    public void addReaction(Reaction reaction) {
        this.reactions.add(reaction);
    }
}
