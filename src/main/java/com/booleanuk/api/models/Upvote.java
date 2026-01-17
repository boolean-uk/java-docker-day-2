package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "upvotes")
public class Upvote {
    @EmbeddedId
    private UpvoteId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"upvotes", "user"})
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"upvotes", "posts"})
    private User user;

    public Upvote(Post post, User user) {
        this.post = post;
        this.user = user;
        this.id = new UpvoteId(post.getId(), user.getId());
    }

    public String toString() {
        return "Upvote(postId=" + post.getId() + ", userId=" + user.getId() + ", ID" + this.getId() +")";
    }
}
