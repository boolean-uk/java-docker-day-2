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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String aboutMe;

    @OneToMany(mappedBy = "originalPoster", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"originalPoster", "parentPost", "likes"})
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Like> likes;

    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yy-HH:mm:ss")
    private LocalDateTime createdAt;

    public User(String name, String aboutMe) {
        this.name = name;
        this.aboutMe = aboutMe;
        this.posts = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    @JsonIgnore
    public boolean isInvalid() {
        return (StringUtils.isBlank(name) || StringUtils.isBlank(aboutMe));
    }
}
