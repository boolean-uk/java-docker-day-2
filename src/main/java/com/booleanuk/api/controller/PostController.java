package com.booleanuk.api.controller;


import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<PostListResponse> getAllPosts() {
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(this.postRepository.findAll());
        return ResponseEntity.ok(postListResponse);
    }


}
