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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public List<Post> getAllPosts() {
        return this.postRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostByID(@PathVariable int id) {
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found by ID"));
        return ResponseEntity.ok(post);
    }


    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {

        User userToPost = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found by ID"));


        post.setUser(userToPost);
        return ResponseEntity.ok(postRepository.save(post));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post post) {

        Post postToUpdate = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found by ID"));
        User user = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found by ID"));

        postToUpdate.setUser(user);

        return new ResponseEntity<Post>(postRepository.save(postToUpdate), HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable int id) {
        Post postToDelete = this.postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found by ID"));
        this.postRepository.delete(postToDelete);

        return ResponseEntity.ok(postToDelete);
    }

}
