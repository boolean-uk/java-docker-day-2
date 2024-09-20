package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIncludeProperties(value = {"content", "likes"})
    private Post post;

    public Comment(int id){
        this.id = id;
    }
}
