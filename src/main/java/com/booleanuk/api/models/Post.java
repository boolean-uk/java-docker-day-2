package com.booleanuk.api.models;


import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Clock;

@Setter
@Getter
@Entity
@Table(name = "post")
public class Post extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String message; // to make things simple, we only have text on posts. no images or videos
    @Column
    private String createdAt;
    @Column
    private Boolean edited;

    @ManyToOne
    @JoinColumn
    @JsonIncludeProperties(value = { "username" })
    private User user;

    public Post() {
        createdAt = Clock.systemUTC().instant().toString();
    }

    public Post(final String message) {
        this.message = message;

        createdAt = Clock.systemUTC().instant().toString();
        edited = false;
    }

    public Post(final Integer id, final String message) {
        this.id = id;
        this.message = message;

        createdAt = Clock.systemUTC().instant().toString();
        edited = false;
    }

    public Post(final Integer id, final String message, final String createdAt, final Boolean edited) {
        this.id = id;
        this.message = message;
        this.createdAt = Clock.systemUTC().instant().toString();
        this.edited = edited;
    }

    @Override
    public boolean haveNullFields() {
        return message == null || createdAt == null || edited == null;
    }

    @Override
    public void copyOverData(Model model) {
        Post _other = (Post) model;

        message = _other.message;
        createdAt = _other.createdAt;
        edited = _other.edited;
    }
}
