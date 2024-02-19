package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(this.postRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        if (post.getContent() == null) {
            return new ResponseEntity<>("The post needs content", HttpStatus.BAD_REQUEST);
        }
        post.setPostedAt(LocalDateTime.now());
        post.setUpdatedAT(LocalDateTime.now());
        return new ResponseEntity<>(this.postRepository.save(post), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOnePost(@PathVariable int id) {
        Post post = this.postRepository.findById(id).orElse(null);
        if (post == null) {
            return new ResponseEntity<>("No post with that id found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable int id, @RequestBody Post post) {
        Post postToUpdate = this.postRepository.findById(id).orElse(null);
        if (postToUpdate == null) {
            return new ResponseEntity<>("No post with that id found", HttpStatus.NOT_FOUND);
        }
        if (post.getContent() == null) {
            return new ResponseEntity<>("The post needs content", HttpStatus.BAD_REQUEST);
        }
        postToUpdate.setContent(post.getContent());
        postToUpdate.setUpdatedAT(LocalDateTime.now());
        return new ResponseEntity<>(this.postRepository.save(postToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        Post postToDelete = this.postRepository.findById(id).orElse(null);
        if (postToDelete == null) {
            return new ResponseEntity<>("No post with that id found", HttpStatus.NOT_FOUND);
        }
        this.postRepository.delete(postToDelete);
        return ResponseEntity.ok(postToDelete);
    }
}
