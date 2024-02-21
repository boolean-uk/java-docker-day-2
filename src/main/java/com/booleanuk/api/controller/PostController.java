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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<PostListResponse> getAllPosts() {
        // Make response with the posts
        PostListResponse response = new PostListResponse();
        response.set(this.postRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Response<?>> getAllPostsByUser(@PathVariable int id) {
        // Check if user with given id exists
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Find all posts made by user
        List<Post> posts = this.postRepository.findAll().stream().filter(p -> p.getUser() == user).toList();
        // Make response with the posts
        PostListResponse response = new PostListResponse();
        response.set(posts);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<Response<?>> createPost(@PathVariable int id, @RequestBody Post post) {
        // Check if user with given id exists
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Check that the post has some content
        if (post.getContent() == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("The post needs content");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        // Check if the authenticated user has the same id, if not: not allowed to make post
        String usernameAuthenticated = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!usernameAuthenticated.equals(user.getUsername())) {
            ErrorResponse error = new ErrorResponse();
            error.set("You are only allowed to create/update/delete posts on your own page");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        // Set postedAt and updatedAt
        post.setPostedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        // Set user that made the post
        post.setUser(user);
        // Make response with the created post
        PostResponse response = new PostResponse();
        response.set(this.postRepository.save(post));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getOnePost(@PathVariable int id) {
        // Check if post with given id exists
        Post post = this.postRepository.findById(id).orElse(null);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No post with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Make response with the post
        PostResponse response = new PostResponse();
        response.set(post);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/{user_id}/post/{post_id}")
    public ResponseEntity<?> updatePost(@PathVariable int user_id, @PathVariable int post_id, @RequestBody Post post) {
        // Check if user with given id exists
        User user = this.userRepository.findById(user_id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Check if user is the same as the authenticated one
        String usernameAuthenticated = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!usernameAuthenticated.equals(user.getUsername())) {
            ErrorResponse error = new ErrorResponse();
            error.set("You are only allowed to create/update/delete posts on your own page");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        // Check if post with given id exists
        Post postToUpdate = this.postRepository.findById(post_id).orElse(null);
        if (postToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No post with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Check that there are content to update
        if (post.getContent() == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("The post needs content");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        // Update post
        postToUpdate.setContent(post.getContent());
        postToUpdate.setUpdatedAt(LocalDateTime.now());
        //Make response
        PostResponse response = new PostResponse();
        response.set(this.postRepository.save(postToUpdate));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{user_id}/post/{post_id}")
    public ResponseEntity<?> deletePost(@PathVariable int user_id, @PathVariable int post_id) {
        // Check if user with given id exists
        User user = this.userRepository.findById(user_id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Check if user is the same as the authenticated one
        String usernameAuthenticated = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!usernameAuthenticated.equals(user.getUsername())) {
            ErrorResponse error = new ErrorResponse();
            error.set("You are only allowed to create/update/delete posts on your own page");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        // Check if post with given id exists
        Post postToDelete = this.postRepository.findById(post_id).orElse(null);
        if (postToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No post with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Delete post
        this.postRepository.delete(postToDelete);
        // Make response with the deleted post
        PostResponse response = new PostResponse();
        response.set(postToDelete);
        return ResponseEntity.ok(response);
    }
}
