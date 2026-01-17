package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("users/{userId}/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post, @PathVariable int userId){
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id")
        );
        post.setUser(user);

        return new ResponseEntity<>(this.postRepository.save(post), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Post> getAll(@PathVariable int userId){
        this.userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id")
        );
        return this.postRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getOne(@PathVariable int id, @PathVariable int userId){
        this.userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id")
        );
        Post post = this.postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No post with that id")
        );
        return ResponseEntity.ok(post);
    }

    @PutMapping("{id}")
    public ResponseEntity<Post> update(@PathVariable int id, @RequestBody Post post, @PathVariable int userId){
        this.userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id")
        );
        Post postToUpdate = this.postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No post with that id")
        );
        postToUpdate.setContent(post.getContent());
        postToUpdate.setLikes(post.getLikes());
        return new ResponseEntity<>(this.postRepository.save(postToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Post> delete(@PathVariable int id){
        Post post = this.postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No post with that id")
        );
        this.postRepository.delete(post);
        return ResponseEntity.ok(post);
    }
}
