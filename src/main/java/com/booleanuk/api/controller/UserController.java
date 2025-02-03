package com.booleanuk.api.controller;

import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.ErrorResponse;
import com.booleanuk.api.response.Response;
import com.booleanuk.api.response.UserListResponse;
import com.booleanuk.api.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private ErrorResponse errorResponse = new ErrorResponse();
    private UserResponse userResponse = new UserResponse();
    private UserListResponse userListResponse = new UserListResponse();

    @GetMapping
    public ResponseEntity<Response<?>> getAllUsers() {
        this.userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser (@RequestBody User user) {
        try {
            this.userResponse.set(this.userRepository.save(user));
        } catch (Exception e) {
            this.errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getOneUser(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = this.userRepository.findById(id).orElse(null);
        if (userToUpdate == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        try {
            userToUpdate = this.userRepository.save(userToUpdate);
        } catch (Exception e) {
            this.errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        this.userResponse.set(userToUpdate);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id) {
        User userToDelete = this.userRepository.findById(id).orElse(null);
        if (userToDelete == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.userRepository.delete(userToDelete);
        this.userResponse.set(userToDelete);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<Response<?>> getAllFriends(@PathVariable int id) {
        User userWithFriends = this.userRepository.findById(id).orElse(null);
        if (userWithFriends == null) {
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.userListResponse.set(userWithFriends.getFriends());
        return ResponseEntity.ok(userListResponse);
    }

    @PostMapping("{id}/friends")
    public ResponseEntity<Response<?>> addFriend(@PathVariable int id, @RequestBody User friend) {
        User userWithFriends = this.userRepository.findById(id).orElse(null);
        if (userWithFriends == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        userWithFriends.getFriends().add(friend);
        this.userResponse.set(friend);
        return ResponseEntity.ok(userResponse);
    }

    // redundant? as friend can be found as any other user?
    @GetMapping("{user_id}/friends/{friend_id}")
    public ResponseEntity<Response<?>> getOneFriend(@PathVariable int userId, @PathVariable int friendId) {
        User user = this.userRepository.findById(userId).orElse(null);
        User friend = this.userRepository.findById(friendId).orElse(null);
        if (user == null || friend == null) {
            this.errorResponse.set("User or friend not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.userResponse.set(friend);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("{user_id}/friends/{friend_id}")
    public ResponseEntity<Response<?>> removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        User user = this.userRepository.findById(userId).orElse(null);
        User friend = this.userRepository.findById(friendId).orElse(null);
        if (user == null || friend == null) {
            this.errorResponse.set("User or friend not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        user.getFriends().remove(friend);
        this.userResponse.set(friend);
        return ResponseEntity.ok(userResponse);
    }
}
