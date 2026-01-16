package com.booleanuk.api.controllers;

import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        User user = userRepository.findByUsername(username);
        if(!userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user){
        if(user.getFirstName() == null || user.getLastName() == null || user.getUsername() == null || user.getEmail() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
        }
        if(userRepository.findByUsername(user.getUsername()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        if(user.getCreated() == null){
            user.setCreated(java.time.OffsetDateTime.now());
        }

        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{username}/update")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody User user){
        User userToUpdate = userRepository.findByUsername(username);
        if(userToUpdate == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if(user.getFirstName() != null){
            userToUpdate.setFirstName(user.getFirstName());
        }
        if(user.getLastName() != null){
            userToUpdate.setLastName(user.getLastName());
        }
        if(user.getEmail() != null){
            userToUpdate.setEmail(user.getEmail());
        }
        userRepository.save(userToUpdate);
        return new ResponseEntity<>(userToUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{username}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
