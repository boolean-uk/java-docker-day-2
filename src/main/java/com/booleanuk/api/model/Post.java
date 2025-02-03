package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public String contents;

    @ManyToOne
    @JoinColumn(name = "originalPosterId")
    @JsonIgnoreProperties({"aboutMe", "posts"})
    public User originalPoster;

    @ManyToOne
    @JoinColumn(name = "parentPostId")
    @JsonIgnoreProperties({"originalPoster", "parentPost", "comments", "likes", "createdAt"})
    public Post parentPost;

    @OneToMany(mappedBy = "parentPost", cascade = CascadeType.ALL)
    private List<Post> comments;

    @OneToMany(mappedBy = "likedPost", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Like> likes;

    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yy-HH:mm:ss")
    private LocalDateTime createdAt;


    public Post(String contents, User originalPoster, Post parentPost) {
        this.contents = contents;
        this.originalPoster = originalPoster;
        this.parentPost = parentPost;
        this.comments = new ArrayList<>();
        likes = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    @JsonIgnore
    public boolean isInvalid() {
        return StringUtils.isBlank(contents);
    }
}
