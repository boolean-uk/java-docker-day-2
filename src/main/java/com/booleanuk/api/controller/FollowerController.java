package com.booleanuk.api.controller;

import com.booleanuk.api.model.*;
import com.booleanuk.api.payload.response.*;
import com.booleanuk.api.repository.FollowerRepository;
import com.booleanuk.api.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("followers")
public class FollowerController {
    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<FollowerListResponse> getAllFollows() {
        FollowerListResponse followerListResponse = new FollowerListResponse();
        followerListResponse.set(this.followerRepository.findAll());
        return ResponseEntity.ok(followerListResponse);
    }

    @PostMapping("/{u1}/follow/{u2}")
    public ResponseEntity<Response<?>> follow(@RequestBody Follower follower, @PathVariable("u1") int u1, @PathVariable("u2") int u2) {
        User user = this.userRepository.findById(u1).orElse(null);
        User isfollowed = this.userRepository.findById(u2).orElse(null);

        try {
            if(user == null || isfollowed == null){
                ErrorResponse error = new ErrorResponse();
                error.set("not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }

            for(Follower aFollower : this.followerRepository.findAll()){
                if(aFollower.getIsFollowing().equals(user) && aFollower.getIsFollowed().equals(isfollowed)){
                    ErrorResponse error = new ErrorResponse();
                    error.set("Bad request");
                    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                }
            }

            follower.setIsFollowing(user);
            follower.setIsFollowed(isfollowed);

            FollowerResponse followerResponse = new FollowerResponse();
            followerResponse.set(this.followerRepository.save(follower));

            return new ResponseEntity<>(followerResponse, HttpStatus.CREATED);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }





    @DeleteMapping("/{u1}/unfollow/{u2}")
    public ResponseEntity<Response<?>> unfollow(@PathVariable("u1") int u1, @PathVariable("u2") int u2) {
        User user = this.userRepository.findById(u1).orElse(null);
        User isfollowed = this.userRepository.findById(u2).orElse(null);

        try {
            if(user == null || isfollowed == null){
                ErrorResponse error = new ErrorResponse();
                error.set("not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }

            for(Follower aFollower : this.followerRepository.findAll()){
                if(aFollower.getIsFollowing().equals(user) && aFollower.getIsFollowed().equals(isfollowed)){
                    this.followerRepository.delete(aFollower);
                    FollowerResponse followerResponse = new FollowerResponse();
                    followerResponse.set(aFollower);
                    return ResponseEntity.ok(followerResponse);
                }
            }

            ErrorResponse error = new ErrorResponse();
            error.set("Bad request, user with ID" + u1 + "do not follow user with ID" + u2);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAllFollowsForSpecificUser(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if(user == null){
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        FollowerListResponse followerListResponse = new FollowerListResponse();
        followerListResponse.set(this.followerRepository.findAllIsFollowingByIsFollowed(user));
        return ResponseEntity.ok(followerListResponse);
    }

    @GetMapping("follows/{id}")
    public ResponseEntity<?> getAllFollowingForSpecificUser(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if(user == null){
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        FollowerListResponse followerListResponse = new FollowerListResponse();
        followerListResponse.set(this.followerRepository.findAllIsFollowedByIsFollowing(user));
        return ResponseEntity.ok(followerListResponse);
    }
}
