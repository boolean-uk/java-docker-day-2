package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnoreProperties({"aboutMe", "posts", "likes", "createdAt"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "likedPost", nullable = false)
    @JsonIgnoreProperties({"originalPoster", "parentPost", "likes", "comments", "createdAt"})
    private Post likedPost;

    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yy-HH:mm:ss")
    private LocalDateTime createdAt;

    public Like(User user, Post likedPost) {
        this.user = user;
        this.likedPost = likedPost;
        this.createdAt = LocalDateTime.now();
    }
}
