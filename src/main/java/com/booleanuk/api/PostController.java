package com.booleanuk.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    PostRepository postRepository;
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post, @RequestParam int userId){
        User author = this.userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        post.setAuthor(author);

        return new ResponseEntity<>(postRepository.save(post), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAll(){
        return ResponseEntity.ok(postRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable int id) {
        Post post = this.postRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(post);
    }
    @PostMapping("/{id}/like/{userId}")
    public ResponseEntity<Post> like(@PathVariable int id, @PathVariable int userId) {
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        post.getLikes().add(user);
        Post updatedPost = this.postRepository.save(post);

        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Post> update(@PathVariable int id, @RequestBody Post post){
        Post toUpdate = postRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        toUpdate.setTitle(post.getTitle());
        toUpdate.setContent(post.getContent());
        postRepository.save(toUpdate);

        return new ResponseEntity<>(toUpdate, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Post> delete(@PathVariable int id){
        Post toDelete = postRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        postRepository.delete(toDelete);

        return ResponseEntity.ok(toDelete);
    }
}