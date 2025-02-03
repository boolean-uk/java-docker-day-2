package com.booleanuk.api.controller;

import com.booleanuk.api.model.Follower;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.Upvote;
import com.booleanuk.api.model.User;
import com.booleanuk.api.payload.response.*;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UpvoteRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("upvotes/{pId}")
public class UpvoteController {
    @Autowired
    private UpvoteRepository upvoteRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{uId}")
    public ResponseEntity<Response<?>> createUpvote(@RequestBody Upvote upvote, @PathVariable("pId") int postId, @PathVariable("uId") int userId) {
        Post post = this.postRepository.findById(postId).orElse(null);
        User user = this.userRepository.findById(userId).orElse(null);


        try {
            if(user == null || post == null){
                ErrorResponse error = new ErrorResponse();
                error.set("not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }

            for(Upvote aUpvote : this.upvoteRepository.findAll()){
                if(aUpvote.getPost().equals(post) && aUpvote.getUser().equals(user)){
                    ErrorResponse error = new ErrorResponse();
                    error.set("Bad request, post is already upvoted");
                    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                }
            }
            upvote.setPost(post);
            upvote.setUser(user);

            UpvoteResponse upvoteResponse = new UpvoteResponse();
            upvoteResponse.set(this.upvoteRepository.save(upvote));
            return new ResponseEntity<>(upvoteResponse, HttpStatus.CREATED);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }





    @DeleteMapping("/{uId}")
    public ResponseEntity<Response<?>> deleteUpvote(@PathVariable("pId") int postId, @PathVariable("uId") int userId) {
        Post post = this.postRepository.findById(postId).orElse(null);
        User user = this.userRepository.findById(userId).orElse(null);

        try {
            if(user == null || post == null){
                ErrorResponse error = new ErrorResponse();
                error.set("not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }

            for(Upvote aUpvote : this.upvoteRepository.findAll()){
                if(aUpvote.getPost().equals(post) && aUpvote.getUser().equals(user)){
                    this.upvoteRepository.delete(aUpvote);
                    UpvoteResponse upvoteResponse = new UpvoteResponse();
                    upvoteResponse.set(aUpvote);
                    return ResponseEntity.ok(upvoteResponse);
                }
            }

            ErrorResponse error = new ErrorResponse();
            error.set("Bad request, user with ID" + userId + "have not upvoted post with ID" + postId);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public ResponseEntity<?> getAllUpvotesForSpecificPost(@PathVariable("pId") int id) {
        Post post = this.postRepository.findById(id).orElse(null);
        if(post == null){
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        UpvoteListResponse upvoteListResponse = new UpvoteListResponse();
        upvoteListResponse.set(this.upvoteRepository.findAllUserByPost(post));
        return ResponseEntity.ok(upvoteListResponse);
    }

}
