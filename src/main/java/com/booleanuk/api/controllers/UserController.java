package com.booleanuk.api.controllers;

import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
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

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user){
        return new ResponseEntity<>(this.userRepository.save(user), HttpStatus.CREATED);
    }

    @GetMapping
    public List<User> getAll(){
        return this.userRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getOne(@PathVariable int id){
        User user = this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id")
        );
        return ResponseEntity.ok(user);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id")
        );
        userToUpdate.setUserName(user.getUserName());
        return new ResponseEntity<>(this.userRepository.save(userToUpdate), HttpStatus.CREATED);
    }

    @PutMapping("{id}/{userId}")
    public ResponseEntity<User> addFriend(@PathVariable int id, @PathVariable int userId) {

        User userToUpdate = this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id")
        );
        User friend = this.userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id")
        );
        List<User> friends = userToUpdate.getUsers();
        friends.add(friend);
        userToUpdate.setUsers(friends);
        return new ResponseEntity<>(this.userRepository.save(userToUpdate), HttpStatus.CREATED);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<User> delete(@PathVariable int id){
        User user = this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id")
        );
        this.userRepository.delete(user);
        return ResponseEntity.ok(user);
    }

}
