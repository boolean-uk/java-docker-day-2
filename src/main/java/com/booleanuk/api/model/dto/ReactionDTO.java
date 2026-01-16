package com.booleanuk.api.model.dto;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReactionDTO {
    private int post;
    private int byUser;
    private String reaction;
}
