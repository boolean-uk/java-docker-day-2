package com.booleanuk.api.controller;

import com.booleanuk.api.model.Like;
import com.booleanuk.api.model.User;
import com.booleanuk.api.model.UserFollow;
import com.booleanuk.api.payload.response.*;
import com.booleanuk.api.repository.UserFollowRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("follows")
public class UserFollowController {
    @Autowired
    UserFollowRepository repository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/followers/list/{followId}")
    public ResponseEntity<Response<?>> getFollowers(@PathVariable int followId) {
        List<UserFollow> userFollows = this.repository.findByFollowId(followId);
        UserFollowListResponse userFollowListResponse = new UserFollowListResponse();
        userFollowListResponse.set(userFollows);
        return ResponseEntity.ok(userFollowListResponse);
    }

    @GetMapping("/following/list/{userId}")
    public ResponseEntity<Response<?>> getFollowing(@PathVariable int userId) {
        List<UserFollow> userFollows = this.repository.findByUserId(userId);
        UserFollowListResponse userFollowListResponse = new UserFollowListResponse();
        userFollowListResponse.set(userFollows);
        return ResponseEntity.ok(userFollowListResponse);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<Response<?>> getNumberOfFollowers(@PathVariable int followId) {
        int followers = this.repository.findAmountOfFollowers(followId);
        StringResponse stringResponse = new StringResponse();
        stringResponse.set("Number of followers: "+followers);
        return ResponseEntity.ok(stringResponse);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<Response<?>> getNumberOfFollowing(@PathVariable int userId) {
        int followers = this.repository.findAmountOfFollowing(userId);
        StringResponse stringResponse = new StringResponse();
        stringResponse.set("Number of following: "+followers);
        return ResponseEntity.ok(stringResponse);
    }

    @PostMapping("/{followId}/users/{userId}")
    public ResponseEntity<Response<?>> followUserForUser(@PathVariable int userId, @PathVariable int followId) {
        User user = this.userRepository.findById(userId).orElse(null);
        User follow = this.userRepository.findById(followId).orElse(null);
        if (user == null || follow == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        if (this.repository.existsByUserIdAndFollowId(userId, followId)) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        UserFollow userFollow = new UserFollow(user, follow);
        UserFollowResponse userFollowResponse = new UserFollowResponse();
        try {
            userFollowResponse.set(this.repository.save(userFollow));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @DeleteMapping("/{followId}/users/{userId}")
    public ResponseEntity<Response<?>> unfollowUserForUser(@PathVariable int userId, @PathVariable int followId) {
        UserFollow userFollowToDelete = this.repository.findByUserIdAndFollowId(userId, followId).orElse(null);
        if (userFollowToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.repository.delete(userFollowToDelete);
        UserFollowResponse userFollowResponse = new UserFollowResponse();
        userFollowResponse.set(userFollowToDelete);
        return ResponseEntity.ok(userFollowResponse);
    }
}
