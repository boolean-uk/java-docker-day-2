package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserRepository userRepository;
    private PostRepository postRepository;

    public UserController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user) {
        User userToUpdate = userRepository.findByUsername(username);
        if (userToUpdate == null) return null;
        user.setUsername(user.getUsername());
        return userRepository.save(userToUpdate);
    }

    @GetMapping("/{username}/posts")
    public List<Post> getAllPostsByUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) return null;
        return postRepository.findAllByUser(user);
    }
}
