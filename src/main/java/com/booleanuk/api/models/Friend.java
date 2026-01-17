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
@Table(name = "friends")
public class Friend {
    @EmbeddedId
    private FriendId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"friends", "posts"})
    private User user;

    @ManyToOne
    @MapsId("friendId")
    @JoinColumn(name = "friend_id")
    @JsonIgnoreProperties({"friends", "posts"})
    private User friend;

    private String status; // "pending", "accepted", "blocked"

    public Friend(User user, User friend, String status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
        this.id = new FriendId(user.getId(), friend.getId());
    }

    public Friend(User user, User friend) {
        this(user, friend, "pending");
    }

    public String toString() {
        return "Friend(userId=" + user.getId() + ", friendId=" + friend.getId() + ", status=" + status + ")";
    }
}
