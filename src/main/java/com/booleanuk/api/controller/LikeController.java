package com.booleanuk.api.controller;

import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("likes")
public class LikeController {
    @Autowired
    LikeRepository repository;


    @GetMapping("/posts/{postId}")
    public ResponseEntity<Response<?>> getLikesForPosts() {return null;}

    @PostMapping("/posts/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> likePost() {return null;}

    @DeleteMapping("/posts/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> unlikePost() {return null;}
}
