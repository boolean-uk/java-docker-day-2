package com.booleanuk.api.controllers;

import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(this.userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable int id) {
        User user = null;
        user = this.userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return new ResponseEntity<User>(this.userRepository.save(user), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
        User updated = this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        updated.setEmail(user.getEmail());
        updated.setFirstName(user.getFirstName());
        updated.setLastName(user.getLastName());
        updated.setUsername(user.getUsername());
        updated.setPhone(user.getPhone());

        return new ResponseEntity<User>(this.userRepository.save(updated), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable int id) {
        User delete = this.userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        this.userRepository.delete(delete);

        return ResponseEntity.ok(delete);
    }
}
