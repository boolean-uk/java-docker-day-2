package com.booleanuk.api.controllers;


import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.apache.coyote.Response;
import org.aspectj.apache.bcel.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return new ResponseEntity<>(this.postRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        User user = this.userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user"));
        post.setUser(user);
        return new ResponseEntity<>(this.postRepository.save(post), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Post> updatePost(@PathVariable(name = "id") int id, @RequestBody Post post) {
        Post postToUpdate = this.postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user"));
        postToUpdate.setPostBody(post.getPostBody());
        return new ResponseEntity<>(this.postRepository.save(postToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Post> deletePost(@PathVariable(name = "id") int id) {
        Post toDelete = this.postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        this.postRepository.delete(toDelete);
        return ResponseEntity.ok(toDelete);
    }


}
