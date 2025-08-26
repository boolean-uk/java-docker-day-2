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

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private final PostRepository repository;

    @Autowired
    private UserRepository userRepository;

    public PostController(PostRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(this.repository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.repository.findById(id).orElseThrow());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> createPost(@RequestBody Post post) {
        User user = this.userRepository.findById(post.getUser().getId()).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found..")
        );
        post.setUser(user);

        return new ResponseEntity<>(this.repository.save(post),HttpStatus.CREATED);
    }

    // Update post
    @PutMapping("{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Integer id,
                                           @RequestBody Post post) {
        Post postToUpdate = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existing post with that ID found")
        );
        postToUpdate.setContent(post.getContent());

        return new ResponseEntity<>(this.repository.save(postToUpdate),
                HttpStatus.CREATED);
    }

    // User upvotes post
    @PutMapping("{id}/upvote/{uId}")
    public ResponseEntity<Post> upvotePost(@PathVariable("id") Integer id,
                                           @PathVariable("uId") Integer uId) {
        Post postToUpvote = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existing post with that ID found")
        );
        User user = this.userRepository.findById(uId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No user with that ID found")
        );

        List<User> currentUpvotes = postToUpvote.getUserUpvotes();

        if(!currentUpvotes.contains(user)){
            currentUpvotes.add(user);
        }
        postToUpvote.setUserUpvotes(currentUpvotes);

        return new ResponseEntity<>(this.repository.save(postToUpvote),
                HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Post> deletePost(@PathVariable("id") Integer id) {
        Post postToDelete = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No post with that ID were found")
        );
        this.repository.delete(postToDelete);
        return ResponseEntity.ok(postToDelete);
    }
}
