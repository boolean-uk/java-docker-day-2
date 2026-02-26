package com.booleanuk.api.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("{userId}")
    public List<Post> getPostsByUser(@PathVariable int userId) {
        List<Post> posts = new ArrayList<>();
        for (Post p : getAllPosts()) {
            if (p.getUser().getId() == userId) {
                posts.add(p);
            }
        }

        return posts;
    }

    @PostMapping("{userId}")
    public Post addPost(@PathVariable int userId, @RequestBody Post post) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with this id"));
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now().toString());

        return postRepository.save(post);
    }

    @PutMapping("{postId}")
    public Post updatePost(@PathVariable int postId, @RequestBody Post post) {
        Post postToUpdate = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find post with this id"));
        postToUpdate.setText(post.getText());

        return postRepository.save(postToUpdate);
    }

    @DeleteMapping("{postId}")
    public Post deletePost(@PathVariable int postId) {
        Post postToDelete = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find post with this id"));

        postRepository.delete(postToDelete);
        return postToDelete;
    }
}
