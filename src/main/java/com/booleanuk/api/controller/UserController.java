package com.booleanuk.api.controller;

import com.booleanuk.api.config.ModelMapperConfig;
import com.booleanuk.api.model.DTO.PostSingleDTO;
import com.booleanuk.api.model.DTO.UserAllDTO;
import com.booleanuk.api.model.DTO.UserSingleDTO;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<UserAllDTO>> getAll() {
        List<User> users = this.userRepository.findAll();
        List<UserAllDTO> userAllDTOS = new ArrayList<>();
        for (User user : users) {
            userAllDTOS.add(modelMapper.map(user, UserAllDTO.class));
        }
        return new ResponseEntity<>(userAllDTOS, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAllDTO> getById(@PathVariable int id) {
        return this.userRepository.findById(id)
                .map(user -> new ResponseEntity<>(modelMapper.map(user, UserAllDTO.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return new ResponseEntity<>(this.userRepository.save(user), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/follow/{followedId}")
    public ResponseEntity<UserSingleDTO> follow(@PathVariable int id, @PathVariable int followedId) {
        User follower = this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User followed = this.userRepository
                .findById(followedId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        followed.getFollowers().add(follower);
        this.userRepository.save(followed);
        return new ResponseEntity<>(modelMapper.map(follower, UserSingleDTO.class), HttpStatus.OK);
    }
    @PostMapping("/{id}/unfollow/{followedId}")
    public ResponseEntity<UserSingleDTO> unfollow(@PathVariable int id, @PathVariable int followedId) {
        User follower = this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User followed = this.userRepository
                .findById(followedId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        follower.getFollowers().remove(followed);
        this.userRepository.save(follower);
        return new ResponseEntity<>(modelMapper.map(follower, UserSingleDTO.class), HttpStatus.OK);
    }

    @PostMapping("/{id}/repost/{postId}")
    public ResponseEntity<UserSingleDTO> repost(@PathVariable int id, @PathVariable int postId) {
        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getReposts().add(this.postRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found")));

        return new ResponseEntity<>(modelMapper.map(this.userRepository.save(user), UserSingleDTO.class), HttpStatus.OK);
    }



    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = this.userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());
        return new ResponseEntity<>(this.userRepository.save(userToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable int id) {
        User userToDelete = this.userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            this.userRepository.deleteById(id);
            return new ResponseEntity<>(userToDelete, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has posts. Cannot delete.");
        }

    }
}
