package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn
    @JsonBackReference      // To avoid recursion error
    private User publisher;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Content text of blogpost
    @Column
    private String text;

    @OneToMany
    @JoinColumn
    private List<Comment> comments = new ArrayList<>();

    public BlogPost(String text) {
        this.text = text;
    }
}
