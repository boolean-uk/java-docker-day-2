package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Follower;
import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.*;
import com.booleanuk.api.repositories.FollowerRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class FollowerController {

    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private UserRepository userRepository;



    @DeleteMapping("/unfollow/{id}")
    public ResponseEntity<Response<?>> unfollowUser(@PathVariable int id) {
        Follower followerToDelete = this.followerRepository.findById(id).orElse(null);
        if (followerToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.followerRepository.delete(followerToDelete);
        FollowerResponse followerResponse = new FollowerResponse();
        followerResponse.set(followerToDelete);
        return ResponseEntity.ok(followerResponse);
    }

    @PostMapping("/follow")
    public ResponseEntity<Response<?>> followUser(@RequestBody Follower follow) {
        FollowerResponse followerResponse = new FollowerResponse();
        User follower = this.userRepository.findById(follow.getFollower().getId()).orElse(null);
        User following = this.userRepository.findById(follow.getFollowing().getId()).orElse(null);
        try {
            follow.setFollower(follower);
            follow.setFollowing(following);
            followerResponse.set(this.followerRepository.save(follow));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(followerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<FollowerListRespons> listAllFollowers(@PathVariable int id) {
        FollowerListRespons followerListRespons = new FollowerListRespons();
        followerListRespons.set(this.followerRepository.findAllByFollowingId(id));
        return ResponseEntity.ok(followerListRespons);
    }


}
