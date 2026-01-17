package com.booleanuk.api.controllers;

import com.booleanuk.api.models.BlogPost;
import com.booleanuk.api.models.Comment;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.BlogPostRepository;
import com.booleanuk.api.repositories.CommentRepository;
import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CommentRepository commentRepository;

    // Create user
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody User user) {
        this.userRepository.save(user);
        UserResponse response = new UserResponse();
        response.set(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<UserListResponse> getAll() {
        UserListResponse response = new UserListResponse();
        response.set(this.userRepository.findAll());
        return ResponseEntity.ok(response);
    }

    // Get specific user
    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getSpecific(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("A user with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        UserResponse response = new UserResponse();
        response.set(user);
        return ResponseEntity.ok(response);
    }

    // Update user
    @PutMapping("{id}")
    public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody User user) {
        User originalUser = this.userRepository.findById(id).orElse(null);
        if (originalUser == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        user.setId(id);
        try {
            this.userRepository.save(user);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        UserResponse response = new UserResponse();
        response.set(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Delete user
    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        UserResponse response = new UserResponse();
        response.set(user);

        try {
            this.userRepository.delete(user);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    // Post blogpost for specific user
    @PostMapping("{userId}/blogposts")
    public ResponseEntity<Response<?>> publishBlogPostForUser(
            @PathVariable int userId,
            @RequestBody BlogPost blogpost) {

        try {
            User user = this.userRepository.findById(userId).orElse(null);
            if (user == null) {
                ErrorResponse error = new ErrorResponse();
                error.set("A user with this id was not found.");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }

            // Populate blogpost with publisher info and save
            blogpost.setPublisher(user);
            this.blogPostRepository.save(blogpost);

            // Add blogpost to user and save user
            user.getBlogPosts().add(blogpost);
            this.userRepository.save(user);

            BlogPostResponse response = new BlogPostResponse();
            response.set(blogpost);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("An error occurred: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Post comment on blogpost
    // NOTE: simplified, could/should be done in other ways
    @PostMapping("{userId}/blogposts/{blogpostId}/comment")
    public ResponseEntity<Response<?>> publishCommentOnBlogPostForUser(
            @PathVariable int userId,
            @PathVariable int blogpostId,
            @RequestBody Comment comment) {

        try {
            // Find corresponding user (publisher)
            User user = this.userRepository.findById(userId).orElse(null);
            if (user == null) {
                ErrorResponse error = new ErrorResponse();
                error.set("A user with this id was not found.");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
            comment.setPublisher(user);

            // Find corresponding blogpost
            BlogPost blogpost = this.blogPostRepository.findById(blogpostId).orElse(null);
            if (blogpost == null) {
                ErrorResponse error = new ErrorResponse();
                error.set("A blogpost with this id was not found.");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
            blogpost.getComments().add(comment);
            comment.setBlogPost(blogpost);

            this.commentRepository.save(comment);

            CommentResponse response = new CommentResponse();
            response.set(comment);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("An error occurred: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("blogposts")
    public ResponseEntity<BlogPostListResponse> getAllBlogposts() {
        BlogPostListResponse response = new BlogPostListResponse();
        response.set(this.blogPostRepository.findAll());
        return ResponseEntity.ok(response);
    }

    // Get all comments for a specific blogpost
    @GetMapping("blogposts/{blogpostId}/comments")
    public ResponseEntity<Response<?>> getCommentOnBlogPostForUser(
            @PathVariable int blogpostId) {

        BlogPost blogpost = this.blogPostRepository.findById(blogpostId).orElse(null);
        if (blogpost == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("A blogpost with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        BlogPostResponse response = new BlogPostResponse();
        response.set(blogpost);
        return ResponseEntity.ok(response);
    }

}
