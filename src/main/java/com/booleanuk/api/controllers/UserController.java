package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
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

    public ResponseEntity<User> createUser(@RequestBody User user){


        try {

            return new ResponseEntity<User>(this.userRepository.save(user),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create user, please check all required fields are correct.");
        }


    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(this.userRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getOneUser(@PathVariable int id){
        User user=this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.ok(user);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id,
                                           @RequestBody User user){
        User userToUpdate=this.userRepository.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No user with that ID found")
        );
        userToUpdate.setUserName(user.getUserName());
        userToUpdate.setCreationDate(user.getCreationDate());
        userToUpdate.setNumberOfFollowers(user.getNumberOfFollowers());
        userToUpdate.setNumberOfPosts(user.getNumberOfPosts());
        return new ResponseEntity<User>(this.userRepository.save(userToUpdate
        ), HttpStatus.CREATED);



    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id){
        User userToDelete=this.userRepository.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No user with that ID found")
        );

        this.userRepository.delete(userToDelete);
        return ResponseEntity.ok(userToDelete);
    }
}

