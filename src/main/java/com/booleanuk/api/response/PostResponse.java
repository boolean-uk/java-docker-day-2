package com.booleanuk.api.response;

import com.booleanuk.api.model.Post;

public class PostResponse extends Response<Post> {
    private Post data;

    public PostResponse(Post post) {
        super("success", post);
    }
}