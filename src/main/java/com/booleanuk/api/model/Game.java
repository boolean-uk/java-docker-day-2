package com.booleanuk.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="Games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String title;
    @Column
    private String genre;
    @Column
    private String publisher;
    @Column
    private String developer;
    @Column
    private int releaseYear;
    @Column
    private Boolean isEarlyAccess;


    public Game(Integer id, String title, String genre, String publisher, String developer, int releaseYear, Boolean isEarlyAccess) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.publisher = publisher;
        this.developer = developer;
        this.releaseYear = releaseYear;
        this.isEarlyAccess = isEarlyAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game user = (Game) o;
        return Objects.equals(id, user.id) && Objects.equals(title, user.title) && Objects.equals(genre, user.genre) && Objects.equals(isEarlyAccess, user.isEarlyAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, genre, isEarlyAccess);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", isEarlyAccess=" + isEarlyAccess +
                '}';
    }
}
