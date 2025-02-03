package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Friend;
import com.booleanuk.api.models.FriendId;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.FriendRepository;
import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.response.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    private final Response<Object> response = new Response<>();

    @GetMapping
    public ResponseEntity<?> getAll() {
        response.setSuccess(friendRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/{friendId}")
    public ResponseEntity<?> getById(@PathVariable int userId, @PathVariable int friendId) {
        FriendId id = new FriendId(userId, friendId);

        Optional<Friend> friend = friendRepository.findById(id);
        if (friend.isEmpty()) {
            response.setError("not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.setSuccess(friend.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Friend friend) {
        FriendId id = new FriendId(friend.getUser().getId(), friend.getFriend().getId());
        friend.setId(id);
        friend.setStatus("pending");
        try {
            Friend newFriend = friendRepository.save(friend);
            response.setSuccess(newFriend);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setError("Bad request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}/{friendId}")
    public ResponseEntity<?> update(@PathVariable int userId, @PathVariable int friendId, @RequestBody Friend updatedFriend) {
        FriendId id = new FriendId(userId, friendId);

        Optional<Friend> existingFriendOptional = friendRepository.findById(id);
        System.out.println(existingFriendOptional.toString());
        if (existingFriendOptional.isEmpty()) {
            response.setError("not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Friend existingFriend = existingFriendOptional.get();

        // Load the user and friend entities to ensure they are managed
        User managedUser = userRepository.findById(updatedFriend.getUser().getId()).orElse(null);
        User managedFriend = userRepository.findById(updatedFriend.getFriend().getId()).orElse(null);

        if (managedUser == null || managedFriend == null) {
            response.setError("User or Friend not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        existingFriend.setUser(managedUser);
        existingFriend.setFriend(managedFriend);
        existingFriend.setStatus(updatedFriend.getStatus());

        try {
            Friend updatedEntity = friendRepository.save(existingFriend);
            System.out.println(updatedEntity.toString() + ": After save");
            response.setSuccess(updatedEntity);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setError("Bad request" + e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<?> delete(@PathVariable int userId, @PathVariable int friendId) {
        FriendId id = new FriendId(userId, friendId);

        Friend friendToDelete = friendRepository.findById(id).orElse(null);
        if (friendToDelete == null) {
            response.setError("not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            friendRepository.deleteById(id);
            response.setSuccess(friendToDelete);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setError("Bad request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
