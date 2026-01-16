package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Like;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.LikeRepository;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.response.ErrorResponse;
import com.booleanuk.api.response.LikeListResponse;
import com.booleanuk.api.response.LikeResponse;
import com.booleanuk.api.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/posts/{postId}/likes")
public class LikeController {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Response<?>> getLikesByPost(@PathVariable("postId") int postId) {

        Post post = this.postRepository.findById(postId).orElse(null);
        if (post == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Post not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }


        List<Like> likes = this.likeRepository.findByPost(post);
        if (!likes.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No users have liked this post");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        LikeListResponse likeListResponse = new LikeListResponse();
        likeListResponse.set(likes);
        return ResponseEntity.ok(likeListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createLike(@PathVariable("userId") int userId, @PathVariable("postId") int postId, @RequestBody Like like) {

        Post post = this.postRepository.findById(postId).orElse(null);
        if (post == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Post not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("User not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        if (!this.likeRepository.findByPostAndUser(post, user).isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("User already liked this post");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }


        LikeResponse likeResponse = new LikeResponse();
        like.setUser(user);
        like.setPost(post);
        this.likeRepository.save(like);
        likeResponse.set(like);
        return new ResponseEntity<>(likeResponse, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Response<?>> giveGrade(@PathVariable("userId") int userId, @PathVariable("postId") int postId) {

        Post post = this.postRepository.findById(postId).orElse(null);
        if (post == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Post not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("User not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        List<Like> existingLike = this.likeRepository.findByPostAndUser(post, user);
        if (existingLike.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("User has not liked this post");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        this.likeRepository.delete(existingLike.getFirst());
        LikeResponse likeResponse = new LikeResponse();
        likeResponse.set(existingLike.getFirst());


        return new ResponseEntity<>(likeResponse, HttpStatus.OK);
    }
}
