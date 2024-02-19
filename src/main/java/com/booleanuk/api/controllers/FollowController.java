package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Follow;
import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.payload.response.SuccessResponse;
import com.booleanuk.api.repository.FollowRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class FollowController {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users/{username}/followers")
    public ResponseEntity<Response<?>> getAllUsersFollowers(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        List<User> followers = followRepository.findByFollowing(user)
                .stream()
                .map(Follow::getFollower)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(followers));
    }

    @GetMapping("/users/{username}/following")
    public ResponseEntity<Response<?>> getAllUsersFollowing(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        List<User> following = followRepository.findByFollower(user)
                .stream()
                .map(Follow::getFollowing)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(following));
    }

    //TODO: change so request body instead
    @PostMapping("/users/{follower}/follow/{following}")
    public ResponseEntity<Response<?>> createFollow(@PathVariable String follower, @PathVariable String following) {

        if(follower.equals(following)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("You cannot follow yourself."));
        }

        User followerUser = userRepository.findByUsername(follower).orElse(null);
        User followingUser = userRepository.findByUsername(following).orElse(null);

        if(followerUser == null || followingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        if(followRepository.findByFollowerAndFollowing(followerUser, followingUser).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(follower + " already follows " + following + "."));
        }

        Follow follow = new Follow();

        follow.setFollower(followerUser);
        follow.setFollowing(followingUser);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(followRepository.save(follow)));
    }
//    @PostMapping("/follow")
//    public ResponseEntity<Response<?>> createFollow(@RequestBody Follow follow) {
//        if(follow.getFollowing() == null || follow.getFollower() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("bad request"));
//        }
//
//        if(follow.getFollowing().getUsername().equals(follow.getFollower().getUsername())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("You cannot follow yourself."));
//        }
//
//        User follower = userRepository.findByUsername(follow.getFollower().getUsername()).orElse(null);
//        User following = userRepository.findByUsername(follow.getFollowing().getUsername()).orElse(null);
//
//        if(follower == null || following == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
//        }
//
//        if(followRepository.findByFollowerAndFollowing(follower, following).isPresent()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(follower.getUsername() + " already follows " + following.getUsername()+"."));
//        }
//
//        follow.setFollower(follower);
//        follow.setFollowing(following);
//
//        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(followRepository.save(follow)));
//    }

    @DeleteMapping("/users/{followerUsername}/unfollow/{followingUsername}")
    public ResponseEntity<Response<?>> deleteFollow(@PathVariable String followerUsername, @PathVariable String followingUsername) {
        User follower = userRepository.findByUsername(followerUsername).orElse(null);
        User following = userRepository.findByUsername(followingUsername).orElse(null);

        if(follower == null || following == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        Follow followToDelete = followRepository.findByFollowerAndFollowing(follower, following).orElse(null);

        if(followToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(follower.getUsername() + " doesn't follow " + following.getUsername()+"."));
        }

        followRepository.delete(followToDelete);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(followToDelete));

    }
}
