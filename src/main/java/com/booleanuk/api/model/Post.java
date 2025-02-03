package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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
    private LocalDateTime posted;
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd mm:ss")
    private  LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "repost", cascade = CascadeType.REMOVE)
    private List<Repost> reposts;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post(int id) {
        this.id = id;
    }
}
