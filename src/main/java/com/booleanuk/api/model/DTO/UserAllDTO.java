package com.booleanuk.api.model.DTO;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserAllDTO {
    private int id;

    private String name;

    private String email;

    @JsonIgnoreProperties({"posts", "likes", "author", "reposts"})
    private List<Post> posts;

    @JsonIgnoreProperties({"posts", "likes", "reposts", "author"})
    private List<Post> reposts;

    @JsonIgnoreProperties({"followers", "reposts", "posts"})
    private List<User> followers;
}
