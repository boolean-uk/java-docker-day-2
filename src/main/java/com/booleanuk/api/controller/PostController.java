package com.booleanuk.api.controller;

import com.booleanuk.api.dto.PostDTO;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.Upvote;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UpvoteRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    PostRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UpvoteRepository upvoteRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.repository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAllById(@PathVariable Integer id) {
        Post post = this.repository.findById(id).orElse(null);
        if(post == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostDTO postDTO) {
        User user = this.userRepository.findById(postDTO.getUser()).orElse(null);
        if(postDTO.getMessage() == null || postDTO.getUser() == null){
            return ResponseEntity.badRequest().body(null);
        }
        Post newPost = new Post(postDTO.getMessage(), user);

        this.repository.save(newPost);

        return ResponseEntity.ok(newPost);
    }

    @PostMapping("{post_id}/like/{user_id}")
    public ResponseEntity<?> upvote(@PathVariable(name = "post_id") Integer postId, @PathVariable(name = "user_id") Integer userId){
        User user = this.userRepository.findById(userId).orElse(null);
        Post post = this.repository.findById(postId).orElse(null);

        if(user == null || post == null) return ResponseEntity.badRequest().body(null);

        Upvote upvote = new Upvote(user, post);

        this.upvoteRepository.save(upvote);

        return ResponseEntity.ok(post);
    }
}
