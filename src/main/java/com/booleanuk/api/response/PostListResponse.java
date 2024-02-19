package com.booleanuk.api.response;


import com.booleanuk.api.post.Post;

import java.util.List;

public class PostListResponse extends Response<List<Post>> {

    public PostListResponse(List<Post> data) {
        super("success", data);
    }
}