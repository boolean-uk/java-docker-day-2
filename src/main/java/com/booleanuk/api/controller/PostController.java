package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.PostListResponse;
import com.booleanuk.api.payload.response.PostResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    @GetMapping
    public ResponseEntity<Response<?>> getAllPostsByAlLUsers()  {
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(this.postRepository.findAll());
        return ResponseEntity.ok(postListResponse);
    }

    /*
    TODO
        For extension
    @GetMapping
    public ResponseEntity<Response<?>> getAllPostsByUser(@RequestParam String username) {
        User userPosting = this.userRepository.findByUsername(username).orElse(null);
        if(userPosting == null)
        {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user with that username found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(userPosting.getPosts());
        return
    }
    */

    @PostMapping
    public ResponseEntity<Response<?>> createAPost(@RequestBody Post post, @RequestParam String username)    {
        User userPosting = this.userRepository.findByUsername(username).orElse(null);
        if(userPosting == null)
        {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user with that username found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.postRepository.save(post);
        userPosting.addPost(post);
        this.userRepository.save(userPosting);

        PostResponse postResponse = new PostResponse();
        postResponse.set(post);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deleteAPost(@PathVariable int id)    {
        Post postToDelete = this.postRepository.findById(id).orElse(null);
        if(postToDelete == null)
        {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No post with that id found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        this.postRepository.delete(postToDelete);
        User userWhoPosted = this.userRepository.findById(postToDelete.getUser().getId()).orElse(null);
        if(userWhoPosted == null)   {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("The post specified was posted by a user who is null");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        userWhoPosted.removePost(postToDelete);
        PostResponse postResponse = new PostResponse();
        postResponse.set(postToDelete);
        return ResponseEntity.ok(postResponse);
    }
}
