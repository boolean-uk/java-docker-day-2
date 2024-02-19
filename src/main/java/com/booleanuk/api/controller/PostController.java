package com.booleanuk.api.controller;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.*;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<PostListResponse> getAllPosts() {
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(this.postRepository.findAll());
        return ResponseEntity.ok(postListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createPost(@RequestBody Post post) {
        PostResponse postResponse = new PostResponse();
        try {
            postResponse.set(this.postRepository.save(post));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getPostById(@PathVariable int id) {
        Post post = this.postRepository.findById(id).orElse(null);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }

    // Update a post
    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updatePostById(@PathVariable int id, @RequestBody Post post) {
        Post existingPost = this.postRepository.findById(id).orElse(null);
        if (existingPost == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Check if the current user is the owner of the post
        if (!existingPost.getUser().getUsername().equals(currentPrincipalName)) {
            ErrorResponse error = new ErrorResponse();
            error.set("Unauthorized to update this post");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        // Update the post
        existingPost.setDescription(post.getDescription());
        existingPost.setLike(post.isLike());
        existingPost.setRepost(post.isRepost());
        existingPost.setComment(post.getComment());

        try {
            existingPost = this.postRepository.save(existingPost);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        PostResponse postResponse = new PostResponse();
        postResponse.set(existingPost);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deletePostById(@PathVariable int id) {
        Post postToDelete = this.postRepository.findById(id).orElse(null);
        if (postToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Check if the current user is the owner of the post
        if (!postToDelete.getUser().getUsername().equals(currentPrincipalName)) {
            ErrorResponse error = new ErrorResponse();
            error.set("Unauthorized to delete this post");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        this.postRepository.delete(postToDelete);
        PostResponse postResponse = new PostResponse();
        postResponse.set(postToDelete);
        return ResponseEntity.ok(postResponse);
    }


    @PostMapping("/{postId}/like")
    public ResponseEntity<Response<?>> likePost(@PathVariable int postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Post not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Assuming like is a toggle action
        post.setLike(!post.isLike());
        postRepository.save(post);
        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<Response<?>> commentOnPost(@PathVariable int postId, @RequestBody String comment) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Post not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        post.setComment(comment);
        postRepository.save(post);
        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping("/{postId}/repost")
    public ResponseEntity<Response<?>> repost(@PathVariable int postId, @AuthenticationPrincipal UserDetails userDetails) {
        Post originalPost = postRepository.findById(postId).orElse(null);
        if (originalPost == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Post not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        // Find the authenticated user making the repost request
        User repostingUser = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (repostingUser == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Authenticated user not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        // Create a repost with the same content but associate it with the reposting user
        Post repost = new Post(originalPost.getDescription(), originalPost.isLike(), true, originalPost.getComment());
        repost.setUser(repostingUser); // Associate with the reposting user
        postRepository.save(repost);

        PostResponse postResponse = new PostResponse();
        postResponse.set(repost);
        return ResponseEntity.ok(postResponse);
    }

}
