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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    //Get all posts of all users
    @GetMapping
    public ResponseEntity<PostListResponse> getAllPosts() {
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(this.postRepository.findAll());
        return ResponseEntity.ok(postListResponse);
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<Response<?>> createPost(@RequestBody Post post, @PathVariable int userId) {
        User author = this.getAnAuthor(userId);
        if (author == null){
            ErrorResponse error = new ErrorResponse();
            error.set("User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        post.setUser(author);
        PostResponse postResponse = new PostResponse();
        try {
            postResponse.set(this.postRepository.save(post));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getPostById(@PathVariable int id) {
        Post post = this.getAPost(id);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable int userId, @PathVariable int postId, @RequestBody Post post) {
        Post postToUpdate = this.getAPost(postId);
        User authorThePost = this.getAnAuthor(userId);
        if (postToUpdate == null || authorThePost == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        postToUpdate.setContent(post.getContent());
        postToUpdate.setUpdatedAt(post.getUpdatedAt());

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

    @DeleteMapping("/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int userId, @PathVariable int postId) {
        Post postToDelete = this.getAPost(postId);
        User authorThePost = this.getAnAuthor(userId);
        if (postToDelete == null || authorThePost == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        this.postRepository.delete(postToDelete);
        PostResponse postResponse = new PostResponse();
        postResponse.set(postToDelete);
        return ResponseEntity.ok(postResponse);
    }
    private Post getAPost(int id){
        return this.postRepository.findById(id).orElse(null);
    }
    private User getAnAuthor(int id){
        return this.userRepository.findById(id).orElse(null);
    }
}