package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.response.Error;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    private LocalDateTime time = LocalDateTime.now();

    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        List<User> movies = this.userRepository.findAll();
        return new ResponseEntity<>(new UserListResponse(this.userRepository.findAll()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response> createUser(@RequestBody User user) {
        String nameRegex = "^(([a-zA-z])+(\\s)*)+$";
        String emailRegex = "^[^@]+@[^@]+\\.[^@]+$";
        String phoneRegex = "^(\\+[0-9]{2})*([0-9]{8})$";
        if(user.getName().matches(nameRegex) && user.getEmail().matches(emailRegex) && user.getPhone().matches(phoneRegex)) {
            user.setCreatedAt(time);
            user.setUpdatedAt(time);
            User newUser = this.userRepository.save(user);
            return new ResponseEntity<>(new UserResponse(newUser), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ErrorResponse(new Error("bad request")), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable int id, @RequestBody User user) {
        String nameRegex = "^(([a-zA-z])+(\\s)*)+$";
        String emailRegex = "^[^@]+@[^@]+\\.[^@]+$";
        String phoneRegex = "^(\\+[0-9]{2})*([0-9]{8})$";
        if(user.getName().matches(nameRegex) && user.getEmail().matches(emailRegex) && user.getPhone().matches(phoneRegex)) {
            User userToUpdate = userRepository.findById(id)
                    .orElse(null);
            if(userToUpdate == null) {
                return new ResponseEntity<>(new ErrorResponse(new Error("not found")), HttpStatus.NOT_FOUND);
            }
            userToUpdate.setUpdatedAt(time);
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPhone(user.getPhone());
            return new ResponseEntity<>(new UserResponse(userToUpdate), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ErrorResponse(new Error("bad request")), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable int id) {
        User userToDelete = userRepository.findById(id)
                .orElse(null);
        if(userToDelete == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("not found")), HttpStatus.NOT_FOUND);
        }
        if(!userToDelete.getPosts().isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse(new Error("User has posts, and could not be deleted")), HttpStatus.BAD_REQUEST);
        }
        this.userRepository.delete(userToDelete);
        userToDelete.setPosts(new ArrayList<>());
        return new ResponseEntity<>(new UserResponse(userToDelete), HttpStatus.OK);
    }


    @GetMapping("/{id}/posts")
    public ResponseEntity<Response> getAllPostsFromUser(@PathVariable int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        List<Post> userPosts = user.getPosts();
        if(!userPosts.isEmpty()) {
            return new ResponseEntity<>(new PostListResponse(userPosts), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorResponse(new Error("not found")), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<Response> createPost(@PathVariable int userId, @RequestBody Post post) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        post.setCreatedAt(time);
        post.setUpdatedAt(time);
        post.setUser(user);
        return new ResponseEntity<>(new PostResponse(this.postRepository.save(post)), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/posts/{postId}")
    public ResponseEntity<Response> updatePost(@PathVariable int userId, @PathVariable int postId, @RequestBody Post newPost) {
        User user = userRepository.findById(userId).orElse(null);
        Post post = postRepository.findById(postId).orElse(null);
        if(user == null || post == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("Not found")), HttpStatus.NOT_FOUND);
        }

        post.setUpdatedAt(time);
        post.setContent(newPost.getContent());
        post.setTitle(newPost.getTitle());
        return new ResponseEntity<>(new PostResponse(post), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public ResponseEntity<Response> deletePost(@PathVariable int userId, @PathVariable int postId) {
        User user = userRepository.findById(userId).orElse(null);
        Post post = postRepository.findById(postId).orElse(null);
        if(user == null || post == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("Not found")), HttpStatus.NOT_FOUND);
        }
        if(post.getUser() != user) {
            return new ResponseEntity<>(new ErrorResponse(new Error("Forbidden")), HttpStatus.FORBIDDEN);
        }
        postRepository.delete(post);
        return new ResponseEntity<>(new PostResponse(post), HttpStatus.OK);
    }

}
