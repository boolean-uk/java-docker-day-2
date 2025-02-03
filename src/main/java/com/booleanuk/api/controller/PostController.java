package com.booleanuk.api.controller;

import com.booleanuk.api.model.*;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    public Post getPost(@PathVariable(name="post_id") int postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public record PostUserRecord(String username, String body) {}

    @PostMapping
    public Post createPost(@RequestBody PostUserRecord postUser) {
        User user = userRepository.findByUsername(postUser.username);
        if (user == null) return null;
        Post post = new Post();
        post.setUser(user);
        post.setBody(postUser.body);
        post.setTimestamp(LocalDateTime.now());
        return postRepository.save(post);
    }

    @PostMapping("/{post_id}")
    public Post repost(@PathVariable(name="post_id") int postId, @RequestBody PostUserRecord postUser) {
        User user = userRepository.findByUsername(postUser.username);
        Post originalPost = postRepository.findById(postId).orElseThrow();
        if (user == null) return null;
        Post repost = new Post();
        repost.setUser(user);
        repost.setBody(postUser.body);
        repost.setOriginalPost(originalPost);
        repost.setTimestamp(LocalDateTime.now());
        return postRepository.save(repost);
    }

    @PostMapping("/{post_id}/upvotes")
    public Set<User> upvote(@PathVariable(name="post_id") int postId, @RequestBody User requestUser) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByUsername(requestUser.getUsername());
        if (user == null) return null;
        post.getUpvotes().add(user);
        postRepository.save(post);
        return post.getUpvotes();
    }

    @GetMapping("/{post_id}/upvotes")
    public Set<User> getUpvotes(@PathVariable(name="post_id") int postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return post.getUpvotes();
    }

    // todo: maybe error handling when user has not upvoted or maybe that's ok. maybe different response
    @DeleteMapping("/{post_id}/upvotes")
    public Set<User> removeUpvote(@PathVariable(name="post_id") int postId, @RequestBody User requestUser) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByUsername(requestUser.getUsername());
        if (user == null) return null;
        post.getUpvotes().remove(user);
        postRepository.save(post);
        return post.getUpvotes();
    }
}
