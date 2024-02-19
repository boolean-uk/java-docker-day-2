package com.booleanuk.api.controller;

import com.booleanuk.api.model.Repost;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.RepostResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.payload.response.StringResponse;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.RepostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reposts")
public class RepostController {
    @Autowired
    RepostRepository repository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Response<?>> getRepostsForPost(@PathVariable int postId) {
        int reposts;
        try {
            reposts = this.repository.findAmountOfRepostsForPost(postId);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        StringResponse stringResponse = new StringResponse();
        stringResponse.set("Post has "+reposts+" reposts");
        return ResponseEntity.ok(stringResponse);
    }

    @PostMapping("/posts/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> repostPostForUser(@PathVariable int postId, @PathVariable int userId) {
        Post post = this.postRepository.findById(postId).orElse(null);
        User user = this.userRepository.findById(userId).orElse(null);
        if (post == null || user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Repost repost = new Repost(user, post);

        RepostResponse repostResponse = new RepostResponse();
        try {
            repostResponse.set(this.repository.save(repost));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            System.out.println(e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(repostResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> unrepostPostForUser(@PathVariable int postId, @PathVariable int userId) {
        Repost repostToDelete = this.repository.findByUserIdAndPostId(userId, postId).orElse(null);
        if ( repostToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.repository.delete(repostToDelete);
        RepostResponse repostResponse = new RepostResponse();
        repostResponse.set(repostToDelete);
        return ResponseEntity.ok(repostResponse);
    }
}
