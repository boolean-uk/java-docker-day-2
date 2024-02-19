package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "students")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String message;

//    @Column
//    private LocalDateTime createdAt;
//    @Column
//    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIncludeProperties(value = {"id", "name", "email", "phone"})
    private User user;

    public Post(String message) {
        this.message = message;

//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
    }




}
