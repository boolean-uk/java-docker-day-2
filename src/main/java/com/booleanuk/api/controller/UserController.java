package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User body) {
        return ResponseEntity.ok(this.userRepository.save(body));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(this.userRepository.findAll());
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestParam int userId, @RequestBody User body) {
        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found");
        }
        User userToUpdate = existingUser.get();
        userToUpdate.setUsername(body.getUsername());
        userToUpdate.setPassword(body.getPassword());
        userToUpdate.setBio(body.getBio());
        return ResponseEntity.ok(this.userRepository.save(userToUpdate));

    }

    @DeleteMapping
    public ResponseEntity<User> delete(@RequestParam int userId) {
        return this.userRepository.findById(userId)
                .map(post -> {
                    this.userRepository.delete(post);
                    return new ResponseEntity<>(post, HttpStatus.OK);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found"));
    }
}
