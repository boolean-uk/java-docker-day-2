package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("posts")
public class PostController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestParam int userId, @RequestBody Post body) {
        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }

        body.setUser(existingUser.get());
        Post newPost = this.postRepository.save(body);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        return ResponseEntity.ok(this.postRepository.findAll());
    }

    @PutMapping
    public ResponseEntity<Post> update(@RequestParam int postId, @RequestBody Post body) {
        Optional<Post> existingPost = this.postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post ID not found");
        }
        Post postToUpdate = existingPost.get();
        postToUpdate.setContent(body.getContent());
        return ResponseEntity.ok(this.postRepository.save(postToUpdate));

    }

    @DeleteMapping
    public ResponseEntity<Post> delete(@RequestParam int postId) {
        return this.postRepository.findById(postId)
                .map(post -> {
                    this.postRepository.delete(post);
                    return new ResponseEntity<>(post, HttpStatus.OK);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post ID not found"));
    }
}
