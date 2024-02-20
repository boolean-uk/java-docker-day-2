package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository repository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Object> getAllPosts() {
        PostListResponse listResponse = new PostListResponse();
        listResponse.set(this.repository.findAll());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getPostById(@PathVariable int id) {

        Post post = this.repository.findById(id).orElse(null);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        PostResponse response = new PostResponse();
        response.set(post);
        return ResponseEntity.ok(response);
    }


    record PostRequest(String title, String message) {}
    @PostMapping("/{id}")
    public ResponseEntity<Response<?>> createPost(@PathVariable int id, @RequestBody PostRequest post) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        if (post.title == null || post.message == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        Post finalPost = new Post(post.title(), post.message(), user);
        this.repository.save(finalPost);

        PostResponse response = new PostResponse();
        response.set(finalPost);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateBook(
            @PathVariable int id,
            @RequestBody Post request) {

        Post post = this.repository.findById(id).orElse(null);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        post.setTitle(request.getTitle());
        post.setMessage(request.getMessage());

        try {
            post = this.repository.save(post);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        PostResponse response = new PostResponse();
        response.set(post);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteBook(@PathVariable int id) {
        Post post = this.repository.findById(id).orElse(null);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.repository.delete(post);
        PostResponse response = new PostResponse();
        response.set(post);
        return ResponseEntity.ok(response);
    }
}
