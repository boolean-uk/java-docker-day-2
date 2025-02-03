package com.booleanuk.api.controller;

import com.booleanuk.api.model.*;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private UserRepository userRepository;
    private PostRepository postRepository;

    public PostController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/{post_id}")
    public Post getPost(@PathVariable int postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public record PostUserRecord(String username, String body) {}

    @PostMapping
    public Post createPost(@RequestBody PostUserRecord postRecord) {
        User user = userRepository.findByUsername(postRecord.username);
        if (user == null) return null;
        Post post = new Post();
        post.setUser(user);
        post.setBody(postRecord.body);
        post.setTimestamp(LocalDateTime.now());
        return postRepository.save(post);
    }

    @PostMapping("/{post_id}/{username}")
    public Post repost(@PathVariable int postId, @RequestBody PostUserRecord postUser) {
        User user = userRepository.findByUsername(postUser.username);
        if (user == null) return null;
        Post originalPost = postRepository.findById(postId).orElseThrow();
        Post repost = new Post();
        repost.setUser(user);
        repost.setOriginalPost(originalPost);
        repost.setTimestamp(LocalDateTime.now());
        repost.setBody(postUser.body);
        return postRepository.save(repost);
    }

    @GetMapping("/{username}")
    public List<Post> getPostsByUser(@PathVariable String username) {
        return userRepository.findByUsername(username).getPosts();
    }
}
