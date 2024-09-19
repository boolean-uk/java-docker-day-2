package com.booleanuk.api.controller;

import com.booleanuk.api.model.ApiResponse;
import com.booleanuk.api.model.Friend;
import com.booleanuk.api.model.Like;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.FriendRepository;
import com.booleanuk.api.repository.LikeRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private final UserRepository repository;

    @Autowired
    private final FriendRepository friendRepository;

    public UserController(UserRepository userRepository, FriendRepository friendRepository) {
        this.repository = userRepository;
        this.friendRepository = friendRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create (@RequestBody User userDetails) {
        if(userDetails.isInvalid()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not create a user with the specified parameters.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = new User(userDetails.getName(), userDetails.getAboutMe());
        ApiResponse<User> response = new ApiResponse<>("success", repository.save(user));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("{userId}/friendRequest/{targetId}")
    public ResponseEntity<ApiResponse<?>> createFriendRequest (@PathVariable int userId, @PathVariable int targetId) {
        Optional<User> use = this.repository.findById(userId);
        if(use.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find user with id " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        User user = use.get();

        Optional<User> tar = this.repository.findById(targetId);
        if(tar.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find user with id " + targetId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        User target = tar.get();

        Friend friend = new Friend(user, target);
        this.friendRepository.save(friend);

        ApiResponse<Friend> response = new ApiResponse<>("success", friend);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> readAll() {
        List<User> users = this.repository.findAll();
        ApiResponse<List<User>> response = new ApiResponse<>("success", users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<?>> readOne (@PathVariable int id) {
        Optional<User> use = this.repository.findById(id);
        if(use.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find user with id " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        User user = use.get();
        ApiResponse<User> response = new ApiResponse<>("success", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}/like")
    ResponseEntity<ApiResponse<?>> readOneLike (@PathVariable int id) {
        Optional<User> use = this.repository.findById(id);
        if(use.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find user with id " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        User user = use.get();

        List<Like> likes = user.getLikes();
        ApiResponse<List<Like>> response = new ApiResponse<>("success", likes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<?>> update (@PathVariable int id, @RequestBody User userDetails) {
        if(userDetails.isInvalid()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not create a student with the specified parameters.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<User> use = this.repository.findById(id);

        if(use.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find student with id " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = use.get();
        user.setName(userDetails.getName());
        user.setAboutMe(userDetails.getAboutMe());
        ApiResponse<User> response = new ApiResponse<>("success", repository.save(user));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> delete (@PathVariable int id) {
        Optional<User> use = this.repository.findById(id);

        if(use.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find student with id " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user =  use.get();
        this.repository.delete(user);

        ApiResponse<User> response = new ApiResponse<>("success", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
