package com.booleanuk.api.controller;

import com.booleanuk.api.model.Like;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.model.UserPostKey;
import com.booleanuk.api.payload.response.*;
import com.booleanuk.api.repository.LikeRepository;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserFollowRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("likes")
public class LikeController {
    @Autowired
    LikeRepository repository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/posts/{postId}")
    public ResponseEntity<Response<?>> getLikesForPost(@PathVariable int postId) {
        int likes;
        try {
            likes = this.repository.findAmountOfLikesOnPost(postId);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        StringResponse stringResponse = new StringResponse();
        stringResponse.set("Post has "+likes+" likes");
        return ResponseEntity.ok(stringResponse);
    }

    @PostMapping("/posts/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> likePostForUser(@PathVariable int postId, @PathVariable int userId) {
        Post post = this.postRepository.findById(postId).orElse(null);
        User user = this.userRepository.findById(userId).orElse(null);
        if (post == null || user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Like like = new Like(user, post);
        UserPostKey userPostKey = new UserPostKey();
        userPostKey.setPostId(post.getId());
        userPostKey.setUserId(user.getId());
        like.setId(userPostKey);

        LikeResponse likeResponse = new LikeResponse();
        try {
            likeResponse.set(this.repository.save(like));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            System.out.println(e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(likeResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{postId}/users/{userId}")
    public ResponseEntity<Response<?>> unlikePostForUser(@PathVariable int postId, @PathVariable int userId) {
        Like likeToDelete = this.repository.findByUserIdAndPostId(userId, postId).orElse(null);
        if ( likeToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.repository.delete(likeToDelete);
        LikeResponse likeResponse = new LikeResponse();
        likeResponse.set(likeToDelete);
        return ResponseEntity.ok(likeResponse);
    }
}
