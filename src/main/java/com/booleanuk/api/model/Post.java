package com.booleanuk.api.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

   @Column
   private String image;

   @Column
   private String userComment;

   @Column
   private int likes;

   @Column
   private int shares;

   @Column
   private int comments;

   @ManyToOne
   @JoinColumn(name = "user_id")
   @JsonIgnoreProperties(value = {"username", "followers", "following", "bio"})
   private User user;

    public Post(int id) {
        this.id = id;
    }

    public Post(String image, String user_comment, int likes, int shares, int comments) {
        this.image = image;
        this.userComment = user_comment;
        this.likes = likes;
        this.shares = shares;
        this.comments = comments;
    }
}
