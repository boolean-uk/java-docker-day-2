package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository posts;

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id) {
        Optional<Post> optionalPost = posts.findById(id);
        return optionalPost.map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post updatedPost) {
        if (!posts.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Post existingPost = posts.findById(id).orElse(null);
        if (existingPost != null) {
            existingPost.setContent(updatedPost.getContent());
            existingPost.setUpdatedAt(LocalDateTime.now());
            Post savedPost = posts.save(existingPost);
            return new ResponseEntity<>(savedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable int id) {
        Optional<Post> optionalPost = posts.findById(id);
        if (optionalPost.isPresent()) {
            Post deletedPost = optionalPost.get();
            posts.deleteById(id);
            return new ResponseEntity<>(deletedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
