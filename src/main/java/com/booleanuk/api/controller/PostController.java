package com.booleanuk.api.controller;


import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.Role;
import com.booleanuk.api.model.User;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.PostListResponse;
import com.booleanuk.api.payload.response.PostResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<PostListResponse> getAllPosts(Authentication authentication) {

        PostListResponse listResponse = new PostListResponse();
        listResponse.set(this.postRepository.findAll());
        return ResponseEntity.ok(listResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createPost(@RequestBody Post post, Authentication authentication) {
        PostResponse postResponse = new PostResponse();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        int currentUser = userDetails.getId();
        User target = this.userRepository.findById(currentUser).orElse(null);

        if (target == null) {
            ErrorResponse newError = new ErrorResponse();
            newError.set("current user is null!");
            return new ResponseEntity<>(newError, HttpStatus.NOT_FOUND);

        }

        post.setUser(target);

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

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable int id, @RequestBody Post post, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String currentUser = userDetails.getUsername();

        boolean hasUserRole = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

        boolean hasAdminRole = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        boolean hasBoth = hasAdminRole && hasUserRole;


        Post postToUpdate = this.postRepository.findById(id).orElse(null);
        if (postToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        if (hasUserRole) {
            if (Objects.equals(postToUpdate.getUser().getUsername(), currentUser)) {

                Post prevPost = this.postRepository.findById(id).orElse(null);
                if (prevPost == null) {
                    ErrorResponse error = new ErrorResponse();
                    error.set("not found");
                    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
                }

                if (post.getMessage() == null) {
                    postToUpdate.setMessage(prevPost.getMessage());
                } else {
                    postToUpdate.setMessage(post.getMessage());
                }

                try {
                    postToUpdate = this.postRepository.save(postToUpdate);
                } catch (Exception e) {
                    ErrorResponse error = new ErrorResponse();
                    error.set("Bad request");
                    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                }

                PostResponse postResponse = new PostResponse();
                postResponse.set(postToUpdate);
                return new ResponseEntity<>(postResponse, HttpStatus.CREATED);

            }
            ErrorResponse error = new ErrorResponse();
            error.set("Wont Work here");
            return ResponseEntity.badRequest().body(error);
        }

        if (hasAdminRole) {

                Post prevPost = this.postRepository.findById(id).orElse(null);
                if (prevPost == null) {
                    ErrorResponse error = new ErrorResponse();
                    error.set("not found");
                    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
                }

                if (post.getMessage() == null) {
                    postToUpdate.setMessage(prevPost.getMessage());
                } else {
                    postToUpdate.setMessage(post.getMessage());
                }

                try {
                    postToUpdate = this.postRepository.save(postToUpdate);
                } catch (Exception e) {
                    ErrorResponse error = new ErrorResponse();
                    error.set("Bad request");
                    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                }

                PostResponse postResponse = new PostResponse();
                postResponse.set(postToUpdate);
                return new ResponseEntity<>(postResponse, HttpStatus.CREATED);

        }

        if (hasBoth) {


            Post prevPost = this.postRepository.findById(id).orElse(null);
            if (prevPost == null) {
                ErrorResponse error = new ErrorResponse();
                error.set("not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }

            if (post.getMessage() == null) {
                postToUpdate.setMessage(prevPost.getMessage());
            } else {
                postToUpdate.setMessage(post.getMessage());
            }

            try {
                postToUpdate = this.postRepository.save(postToUpdate);
            } catch (Exception e) {
                ErrorResponse error = new ErrorResponse();
                error.set("Bad request");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

            PostResponse postResponse = new PostResponse();
            postResponse.set(postToUpdate);
            return new ResponseEntity<>(postResponse, HttpStatus.CREATED);

        }
        ErrorResponse lasterror = new ErrorResponse();
        lasterror.set("Somethings wrong");
        return ResponseEntity.badRequest().body(lasterror);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int id) {
        Post postToDelete = this.postRepository.findById(id).orElse(null);
        if (postToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.postRepository.delete(postToDelete);
        PostResponse postResponse = new PostResponse();
        postResponse.set(postToDelete);
        return ResponseEntity.ok(postResponse);
    }
}

