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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;


    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers() {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody User user) {
        //Dates
        LocalDateTime currentDateTime = LocalDateTime.now();
        user.setCreatedAt(currentDateTime);

        UserResponse userResponse = new UserResponse();
        try {
            userResponse.set(this.userRepository.save(user));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getUserById(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = this.userRepository.findById(id).orElse(null);
        if (userToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        userToUpdate.setUserName(user.getUserName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhone(user.getPhone());

        //Set update to right now
        LocalDateTime currentDateTime = LocalDateTime.now();
        userToUpdate.setUpdatedAt(currentDateTime);

        this.userRepository.save(userToUpdate);
        UserResponse userResponse = new UserResponse();
        userResponse.set(userToUpdate);
        return ResponseEntity.ok(userResponse);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id) {
        User userToDelete = this.userRepository.findById(id).orElse(null);
        if (userToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.userRepository.delete(userToDelete);
        UserResponse userResponse = new UserResponse();
        userResponse.set(userToDelete);
        return ResponseEntity.ok(userResponse);
    }


    //Controllers for posts
    //Get all posts by the user (profile)
    @GetMapping("/{id}/posts")
    public ResponseEntity<Response<?>> getAllUserPosts(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        PostListResponse postListResponse = new PostListResponse();
        List<Post> posts = user.getPosts();
        //Sorter from chatgpt to sort the list from newest to oldest
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return post2.getCreatedAt().compareTo(post1.getCreatedAt());
            }
        });
        postListResponse.set(posts);
        return ResponseEntity.ok(postListResponse);
    }
    @PostMapping("/{id}/posts")
    public ResponseEntity<Response<?>> createPost(@PathVariable int id,@RequestBody Post post) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        post.setUser(user);
        //Dates
        LocalDateTime currentDateTime = LocalDateTime.now();
        post.setCreatedAt(currentDateTime);

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

    @GetMapping("/{userId}/posts/{postId}")
    public ResponseEntity<Response<?>> getPostById(@PathVariable int userId,@PathVariable int postId) {
        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        for(Post post : user.getPosts()) {
            if (post.getId()==postId){
                PostResponse postResponse = new PostResponse();
                postResponse.set(post);
                return ResponseEntity.ok(postResponse);
            }
        }
        ErrorResponse error = new ErrorResponse();
        error.set("Post not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{userId}/posts/{postId}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable int userId,@PathVariable int postId, @RequestBody Post post) {
        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        for(Post postToUpdate : user.getPosts()) {
            if (postToUpdate.getId()==postId){
                postToUpdate.setTitle(post.getTitle());
                postToUpdate.setContent(post.getContent());
                //Set update to right now
                LocalDateTime currentDateTime = LocalDateTime.now();
                postToUpdate.setUpdatedAt(currentDateTime);
                this.postRepository.save(postToUpdate);

                PostResponse postResponse = new PostResponse();
                postResponse.set(postToUpdate);
                return ResponseEntity.ok(postResponse);
            }
        }
        ErrorResponse error = new ErrorResponse();
        error.set("Post not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int userId,@PathVariable int postId) {
        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        for(Post postToDelete : user.getPosts()) {
            if (postToDelete.getId()==postId){
                this.postRepository.delete(postToDelete);
                PostResponse postResponse = new PostResponse();
                postResponse.set(postToDelete);
                return ResponseEntity.ok(postResponse);
            }
        }
        ErrorResponse error = new ErrorResponse();
        error.set("Post not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
