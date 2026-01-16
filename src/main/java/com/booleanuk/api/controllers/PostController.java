package com.booleanuk.api.controllers;

import com.booleanuk.api.HelperUtils;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllPosts() {
        List<Post> postList = this.postRepository.findAll();
        return HelperUtils.okRequest(postList);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody Post post) {
        Optional<User> userOptional = this.userRepository.findById(post.getUser().getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            post.setUser(user);

            Post savedPost = this.postRepository.save(post);

            return HelperUtils.createdRequest(savedPost);
        } else {
            return HelperUtils.badRequest(new ApiResponse.Message("No"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deletePost(@PathVariable int id) {
        Optional<Post> postOptional = this.postRepository.findById(id);
        if (postOptional.isPresent()) {
            this.postRepository.deleteById(id);
            postOptional.get().setComments(new ArrayList<>());
            return HelperUtils.okRequest(postOptional);
        } else {
            return HelperUtils.badRequest(new ApiResponse.Message("NO"));
        }
    }
}
