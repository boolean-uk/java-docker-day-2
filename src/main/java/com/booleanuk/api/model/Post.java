package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

@Entity
@Table(name = "users")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message")
    private String message;

//    @Column
//    private LocalDateTime createdAt;
//    @Column
//    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
//    @JsonIncludeProperties(value = {"id", "name", "email", "phone"})


    private com.booleanuk.api.model.User user;

    public Post(String message, User user) {
        this.message = message;
        this.user = user;

//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
    }

    public Post(int id) {
        this.id = id;
    }



}
