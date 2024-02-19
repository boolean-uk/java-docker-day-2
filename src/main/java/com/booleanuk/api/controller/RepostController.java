package com.booleanuk.api.controller;

import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.RepostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("likes")
public class RepostController {
    @Autowired
    RepostRepository repository;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Response<?>> getRepostsForPosts() {return null;}

    @PostMapping("/posts/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> repostPost() {return null;}

    @DeleteMapping("/posts/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> unrepostPost() {return null;}
}
