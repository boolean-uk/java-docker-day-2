package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.ErrorResponse;
import com.booleanuk.api.response.PostListResponse;
import com.booleanuk.api.response.PostResponse;
import com.booleanuk.api.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
@RestController
public class PostController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("users/{userID}/posts")
    public ResponseEntity<Response<?>> getAllPost(@PathVariable int userID){
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        List<Post> posts = this.postRepository.findByUserId(userID);
        if (posts.isEmpty()) {
            ErrorResponse error = new ErrorResponse();
            error.set("No post found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(posts);
        return ResponseEntity.ok(postListResponse);
    }
    @PostMapping("users/{userID}/posts")
    public ResponseEntity<Response<?>> addPost(@PathVariable int userID, @RequestBody Post post){
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Post post1 = new Post();
        try {
            post1.setUser(user);
            post1.setPost(post.getPost());
            post1.setCreatedAt(ZonedDateTime.now());
            post1.setUpdatedAt(ZonedDateTime.now());
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Could not create post for that user");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        this.postRepository.save(post1);
        PostResponse postResponse = new PostResponse();
        postResponse.set(post1);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }
    @PutMapping("users/{userID}/posts/{postID}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable int userID, @PathVariable int postID, @RequestBody Post post){
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Post post1  = this.postRepository.findById(postID).orElse(null);
        if (post1 == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No post with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        try {
            post1.setUser(post.getUser());
            post1.setPost(post.getPost());
            post1.setCreatedAt(post.getCreatedAt());
            post1.setUpdatedAt(ZonedDateTime.now());
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Could not update post for that user");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        this.postRepository.save(post1);
        PostResponse postResponse = new PostResponse();
        postResponse.set(post1);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }
    @DeleteMapping("users/{userID}/posts/{postID}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int userID, @PathVariable int postID, @RequestBody Post post) {
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Post post1 = this.postRepository.findById(postID).orElse(null);
        if (post1 == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No post with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.postRepository.delete(post);
        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }
}
