package com.booleanuk.api.controllers;

import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository repo;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user){
        user.setPosts(new ArrayList<>());
        user.setInteractions(new ArrayList<>());
        user.setCreatedAt(nowFormatted());
        user.setUpdatedAt(nowFormatted());
        repo.save(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getOne(@PathVariable int id){
        User user = getById(id);

        return ResponseEntity.ok(user);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user){
        User toUpdate = getById(id);

        Optional.ofNullable(user.getName())
                .ifPresent(name -> toUpdate.setName(name));
        Optional.ofNullable(user.getUsername())
                .ifPresent(username -> toUpdate.setUsername(username));
        Optional.ofNullable(user.getDob())
                .ifPresent(dob -> toUpdate.setDob(dob));
        Optional.ofNullable(user.getPhone())
                .ifPresent(phone -> toUpdate.setPhone(phone));

        toUpdate.setUpdatedAt(nowFormatted());
        repo.save(toUpdate);

        return new ResponseEntity<>(toUpdate, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> delete(@PathVariable int id){
        User toDelete = getById(id);

        toDelete.setPosts(new ArrayList<>());
        toDelete.setInteractions(new ArrayList<>());
        repo.delete(toDelete);

        return ResponseEntity.ok(toDelete);
    }

    private User getById(int id){
        return repo
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private String nowFormatted(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return now.format(format);
    }
}
