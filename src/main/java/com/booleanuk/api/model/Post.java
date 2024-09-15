package com.booleanuk.api.model;

import com.booleanuk.api.model.User;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String post;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime createdAt = ZonedDateTime.now();
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIncludeProperties(value = {"name"})
    private User user;
}