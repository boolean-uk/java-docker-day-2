package com.booleanuk.api.library.controllers;

import com.booleanuk.api.library.models.*;
import com.booleanuk.api.library.payload.response.ErrorResponse;
import com.booleanuk.api.library.payload.response.PostListResponse;
import com.booleanuk.api.library.payload.response.PostResponse;
import com.booleanuk.api.library.payload.response.Response;
import com.booleanuk.api.library.repository.PostRepository;
import com.booleanuk.api.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<PostListResponse> getAllPostsForCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(user.getPosts());
        return ResponseEntity.ok(postListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createPostForCurrentUser(@RequestBody Post post, @AuthenticationPrincipal UserDetails userDetails) {
        PostResponse postResponse = new PostResponse();

        User user = getCurrentUser(userDetails);
        post.setUser(user);

        try {
            postResponse.set(this.postRepository.save(post));
        } catch (Exception e) {
            return badRequest();
        }
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getPostById(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Post post;
        try {
            post = user.getPosts().get(id);
        } catch (Exception e) {
            return notFound();
        }
        return postResponse(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable int id, @RequestBody Post post, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Post postToUpdate;
        try {
            postToUpdate = user.getPosts().get(id);
        } catch (Exception e) {
            return notFound();
        }
        postToUpdate.setContent(post.getContent());
        try {
            postToUpdate = this.postRepository.save(postToUpdate);
        } catch (Exception e) {
            return badRequest();
        }
        return postResponse(postToUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Post postToDelete;
        try {
            postToDelete = user.getPosts().get(id);
        } catch (Exception e) {
            return notFound();
        }
        this.postRepository.delete(postToDelete);
        return postResponse(postToDelete);
    }

    private Post getAPost(int id){
        return this.postRepository.findById(id).orElse(null);
    }

    private ResponseEntity<Response<?>> badRequest(){
        ErrorResponse error = new ErrorResponse();
        error.set("Could not create Post, please check all required fields are correct");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Response<?>> notFound(){
        ErrorResponse error = new ErrorResponse();
        error.set("No Post with that id were found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Response<?>> postResponse(Post post){
        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }

    private User getCurrentUser(UserDetails userDetails){
        Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
        return userOptional.orElseThrow(() -> new NoSuchElementException("User not found"));
    }

}
