package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.response.ErrorResponse;
import com.booleanuk.api.response.PostListResponse;
import com.booleanuk.api.response.PostResponse;
import com.booleanuk.api.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getPostById(@PathVariable int id) {
        Post post = this.postRepository.findById(id).orElse(null);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int id) {
        if (!this.postRepository.existsById(id)) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        this.postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable int id, @RequestBody Post updatedPost) {
        if (!this.postRepository.existsById(id)) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        Post savedPost = this.postRepository.save(updatedPost);
        PostResponse postResponse = new PostResponse();
        postResponse.set(savedPost);
        return ResponseEntity.ok(postResponse);
    }
    @PostMapping
    public ResponseEntity<Response<?>> addPost(@RequestBody Post newPost) {
        Post savedPost = this.postRepository.save(newPost);
        PostResponse postResponse = new PostResponse();
        postResponse.set(savedPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }
}