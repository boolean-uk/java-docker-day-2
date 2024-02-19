package com.booleanuk.api.response;


import com.booleanuk.api.post.Post;

public class PostResponse extends Response<Post>{

    public PostResponse(Post data) {
        super("success", data);
    }
}