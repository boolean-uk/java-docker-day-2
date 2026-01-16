package com.booleanuk.api.controller;

import com.booleanuk.api.model.User;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository users;

    @Autowired
    private PostRepository posts;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = users.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> optionalUser = users.findById(id);
        if (optionalUser.isPresent()) {
            return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        Optional<User> optionalUser = users.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            LocalDateTime createdAt = existingUser.getCreatedAt();
            user.setId(id);
            user.setCreatedAt(createdAt);
            User updatedUser = users.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        Optional<User> optionalUser = users.findById(id);
        if (optionalUser.isPresent()) {
            User deletedUser = optionalUser.get();
            users.deleteById(id);
            return new ResponseEntity<>(deletedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable int userId) {
        Optional<User> userOptional = users.findById(userId);
        if (userOptional.isPresent()) {
            List<Post> postList = posts.findByUserId(userId);
            if (!postList.isEmpty()) {
                return new ResponseEntity<>(postList, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<Post> createPostByUserId(@PathVariable int userId, @RequestBody Post post) {
        Optional<User> userOptional = users.findById(userId);
        if (userOptional.isPresent()) {
            post.setUserId(userId);
            Post savedPost = posts.save(post);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
