package com.booleanuk.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.models.User;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User addUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @PutMapping("{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setAge(user.getAge());

        return userRepository.save(userToUpdate);
    }

    @DeleteMapping("{id}")
    public User deleteUser(@PathVariable int id) {
        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        userRepository.delete(userToDelete);

        return userToDelete;
    }
}
