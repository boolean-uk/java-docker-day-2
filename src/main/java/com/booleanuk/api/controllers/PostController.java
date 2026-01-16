package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.CustomResponse;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<CustomResponse> getAllPosts() {
        CustomResponse customResponse = new CustomResponse("success", postRepository.findAll());
        return ResponseEntity.ok(customResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse> getPostById(@PathVariable int id) {
        CustomResponse customResponse = new CustomResponse("success", postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found")));
        return ResponseEntity.ok(customResponse);
    }

    @PostMapping
    public ResponseEntity<CustomResponse> createPost(@RequestBody Post post) {
        CustomResponse customResponse = new CustomResponse("success", postRepository.save(post));
        return ResponseEntity.ok(customResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse> updatePost(@PathVariable int id, @RequestBody Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        CustomResponse customResponse = new CustomResponse("success", postRepository.save(existingPost));
        return ResponseEntity.ok(customResponse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> deletePost(@PathVariable int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAuthenticatedName = authentication.getName();
        String postOwnerUsername = post.getUser().getUsername();
        if (!postOwnerUsername.equals(currentAuthenticatedName)) {
            CustomResponse error = new CustomResponse("error", new Error("Unauthorized"));
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        postRepository.delete(post);
        CustomResponse customResponse = new CustomResponse("success", "Post deleted");
        return ResponseEntity.ok(customResponse);
    }
}
