package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("users/{userID}/posts")
    public ResponseEntity<Response<?>> getAll(@PathVariable int userID) {
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        List<Post> posts = this.postRepository.findUserId(userID);
        if (posts.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(posts);
        return ResponseEntity.ok(postListResponse);
    }

    @PostMapping("users/{userID}/posts")
    public ResponseEntity<Response<?>> create(@PathVariable int userID, @RequestBody Post post) {
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Post createPost = new Post();
        try {
            createPost.setUser(user);
            createPost.setPost(post.getPost());
            createPost.setCreated(post.getCreated());
            createPost.setUpdated(post.getUpdated());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        this.postRepository.save(createPost);
        PostResponse postResponse = new PostResponse();
        postResponse.set(createPost);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @PutMapping("users/{userID}/posts/{postID}")
    public ResponseEntity<Response<?>> update(@PathVariable int userID, @PathVariable int postID, @RequestBody Post post) {
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Post updatePost = this.postRepository.findById(postID).orElse(null);
        if (updatePost == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        try {
            updatePost.setUser(post.getUser());
            updatePost.setPost(post.getPost());
            updatePost.setCreated(post.getCreated());
            updatePost.setUpdated(post.getUpdated());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("no post with id");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        this.postRepository.save(updatePost);
        PostResponse postResponse = new PostResponse();
        postResponse.set(updatePost);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("users/{userID}/posts/{postID}")
    public ResponseEntity<Response<?>> delete(@PathVariable int userID, @PathVariable int postID, @RequestBody Post post) {
        User user = this.userRepository.findById(userID).orElse(null);
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Post deletePost = this.postRepository.findById(postID).orElse(null);
        if (deletePost == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        }
        this.postRepository.delete(post);
        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }
}