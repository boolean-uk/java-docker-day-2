package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.CommentRepository;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;


    @GetMapping()
    public ResponseEntity<?> getAll(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return ResponseEntity.ok(postRepository.findByUserUsername(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createPost(@PathVariable int userId, @RequestBody Post post) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(post.getTitle() == null || post.getContent() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
        }

        post.setCreated(java.time.OffsetDateTime.now());
        post.setUser(user);
        postRepository.save(post);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updatePost(@PathVariable int id, @RequestBody Post post) {
        Post postToUpdate = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if(post.getTitle() != null){
            postToUpdate.setTitle(post.getTitle());
        }
        if(post.getContent() != null){
            postToUpdate.setContent(post.getContent());
        }
        postToUpdate.setUpdated(java.time.OffsetDateTime.now());
        postRepository.save(postToUpdate);
        return new ResponseEntity<>(postToUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        // find all comments for this post
        // delete all comments
        for (int i = 0; i < commentRepository.findAll().size(); i++) {
            if(commentRepository.findAll().get(i).getPost().getId() == id) {
                commentRepository.delete(commentRepository.findAll().get(i));
            }
        }
        postRepository.delete(post);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }
}
