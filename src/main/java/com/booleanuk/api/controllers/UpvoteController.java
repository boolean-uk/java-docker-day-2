package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.Upvote;
import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.*;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UpvoteRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UpvoteController {

    @Autowired
    private UpvoteRepository upvoteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;


    @PostMapping("/upvote")
    public ResponseEntity<Response<?>> createAnUpvote(@RequestBody Upvote upvote) {
        UpvoteResponse upvoteResponse = new UpvoteResponse();
        User user = this.userRepository.findById(upvote.getUser().getId()).orElse(null);
        Post post = this.postRepository.findById(upvote.getPost().getId()).orElse(null);

        try {
            upvote.setUser(user);
            upvote.setPost(post);
            upvoteResponse.set(this.upvoteRepository.save(upvote));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(upvoteResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteupvote/{id}")
    public ResponseEntity<Response<?>> deleteUpvote(@PathVariable int id) {
        Upvote upvoteToDelete = this.upvoteRepository.findById(id).orElse(null);
        if (upvoteToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.upvoteRepository.delete(upvoteToDelete);
        UpvoteResponse upvoteResponse = new UpvoteResponse();
        upvoteResponse.set(upvoteToDelete);
        return ResponseEntity.ok(upvoteResponse);
    }

    @GetMapping("/upvotes/{id}")
    public ResponseEntity<UpvoteListResponse> listAllUserThatUpvoted(@PathVariable int id) {
        UpvoteListResponse upvoteListResponse = new UpvoteListResponse();
        upvoteListResponse.set(this.upvoteRepository.findAllByUserId(id));
        return ResponseEntity.ok(upvoteListResponse);
    }
}
