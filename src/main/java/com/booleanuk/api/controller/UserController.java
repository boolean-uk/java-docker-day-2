package com.booleanuk.api.controller;

import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.EnumMap;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(this.repository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable("id") int id) {
        User user = null;
        user = this.repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given id not found"));

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User employee) {
        return new ResponseEntity<User>(this.repository.save(employee), HttpStatus.CREATED); // 201 status
    }

    @PutMapping("{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = this.repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given id not found"));

        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setUserName(user.getUserName());

        return new ResponseEntity<User>(this.repository.save(userToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> delete (@PathVariable int id) {
        User userToDelete = this.repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with given id not found"));

        this.repository.delete(userToDelete);

        return ResponseEntity.ok(userToDelete); // 200 status by default
    }

}
