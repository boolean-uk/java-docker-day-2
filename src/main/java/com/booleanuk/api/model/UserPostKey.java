package com.booleanuk.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UserPostKey implements Serializable {
    @Column(name = "user_id")
    private int userId;

    @Column(name = "post_id")
    private int postId;
}
