package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.PostListResponse;
import com.booleanuk.api.payload.response.PostResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    PostRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/{postId}")
    public ResponseEntity<Response<?>> getPost(@PathVariable int postId) {
        Post post = this.repository.findById(postId).orElse(null);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping
    public ResponseEntity<Response<?>> getPosts(@RequestHeader (name="Authorization") String token) {
        return this.getPostsForUser(this.getUserIdFromToken(token));
    }

    @PostMapping
    public ResponseEntity<Response<?>> create(@RequestBody Post post, @RequestHeader (name="Authorization") String token) {
        return this.createPostForUser(this.getUserIdFromToken(token), post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Response<?>> delete(@PathVariable int postId, @RequestHeader (name="Authorization") String token) {
        User user = this.getUserFromToken(token);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Checks if logged in user is owner of post
        if (user.getPosts().stream().noneMatch(x -> x.getId() == postId)) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return this.deletePostForUser(postId);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Response<?>> update(@PathVariable int postId, @RequestBody Post post, @RequestHeader (name="Authorization") String token) {
        User user = this.getUserFromToken(token);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Checks if logged in user is owner of post
        if (user.getPosts().stream().noneMatch(x -> x.getId() == postId)) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return this.updatePostForUser(postId, post);
    }

    // All down from here only available to ADMINs
    @GetMapping("/users/{userId}")
    public ResponseEntity<Response<?>> getPostsForUser(@PathVariable int userId) {
        List<Post> posts = this.repository.findByUserId(userId).orElse(null);
        if (posts == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(posts);
        return ResponseEntity.ok(postListResponse);
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<Response<?>> createPostForUser(@PathVariable int userId, @RequestBody Post post) {
        PostResponse postResponse = new PostResponse();
        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        try {
            post.setUser(user);
            postResponse.set(this.repository.save(post));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{postId}")
    public ResponseEntity<Response<?>> deletePostForUser(@PathVariable int postId) {
        Post postToDelete = this.repository.findById(postId).orElse(null);
        if (postToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.repository.delete(postToDelete);
        PostResponse postResponse = new PostResponse();
        postResponse.set(postToDelete);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/users/{postId}")
    public ResponseEntity<Response<?>> updatePostForUser(@PathVariable int postId, @RequestBody Post post) {
        Post postToUpdate = this.repository.findById(postId).orElse(null);
        if (postToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        postToUpdate.setText(post.getText());
        postToUpdate.setMedia(post.getMedia());

        try {
            postToUpdate = this.repository.save(postToUpdate);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        PostResponse postResponse = new PostResponse();
        postResponse.set(postToUpdate);
        return ResponseEntity.ok(postResponse);
    }

    public int getUserIdFromToken(String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token.substring(7));
        User user = this.userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return -1;
        }
        return user.getId();
    }

    public User getUserFromToken(String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token.substring(7));
        return this.userRepository.findByUsername(username).orElse(null);
    }
}
