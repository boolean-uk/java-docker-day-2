package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        List<Post> posts = this.postRepository.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable int id) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        Post postToCreate = this.postRepository.save(post);
        // Fill in nullable fields
        this.postRepository.save(postToCreate);
        return new ResponseEntity<>(postToCreate, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable int id, @RequestBody Post post) {
        Post postToUpdate = this.postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        postToUpdate.setUpdatedAt(LocalDateTime.now());
        postToUpdate.setContent(post.getContent());
        return new ResponseEntity<Post>(this.postRepository.save(postToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Post> delete(@PathVariable int id) {
        Post postToDelete = this.postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        this.postRepository.delete(postToDelete);
        return ResponseEntity.ok(postToDelete);
    }
}
