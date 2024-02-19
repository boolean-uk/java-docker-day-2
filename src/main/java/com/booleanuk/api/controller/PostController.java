package com.booleanuk.api.controller;

import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    PostRepository repository;

    @GetMapping
    public ResponseEntity<Response<?>> getAllPosts() {return null;}

    @GetMapping("/{postId}")
    public ResponseEntity<Response<?>> getPost() {return null;}

    @GetMapping("/users/{userId}")
    public ResponseEntity<Response<?>> getPostsForUser() {return null;}

    @PostMapping("/users/{userId}")
    public ResponseEntity<Response<?>> createPostForUser() {return null;}

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Response<?>> deletePostForUser() {return null;}
}
