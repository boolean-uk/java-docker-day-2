package com.booleanuk.api.controller;

import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getUsers() {
        Iterable<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));

    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with that ID found")
        );
        return ResponseEntity.ok(user);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with that ID found")
        );
        user.setId(userToUpdate.getId());
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with that ID found")
        );
        userRepository.delete(user);
        return ResponseEntity.ok(user);
    }

}
