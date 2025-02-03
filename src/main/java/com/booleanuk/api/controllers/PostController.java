package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.*;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("user")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;



    @PostMapping("/post")
    public ResponseEntity<Response<?>> createPost(@RequestBody Post post) {
        PostResponse postResponse = new PostResponse();
        User user = this.userRepository.findById(post.getUser().getId()).orElse(null);

        try {
            post.setUser(user);
            postResponse.set(this.postRepository.save(post));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/deletepost/{id}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int id) {
        Post postToDelete = this.postRepository.findById(id).orElse(null);
        if (postToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.postRepository.delete(postToDelete);
        PostResponse postResponse = new PostResponse();
        postResponse.set(postToDelete);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostListResponse> listAllPostsByUser(@PathVariable int id) {
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(this.postRepository.findAllByUserId(id));
        return ResponseEntity.ok(postListResponse);
    }

    @PutMapping("/updatepost/{id}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable int id, @RequestBody Post post) {
        Post postToUpdate = this.postRepository.findById(id).orElse(null);
        if (postToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        postToUpdate.setContent(post.getContent());
        postToUpdate.setUpdatedAt(LocalDateTime.now());

        try {
            postToUpdate = this.postRepository.save(postToUpdate);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        PostResponse postResponse = new PostResponse();
        postResponse.set(postToUpdate);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }
}
