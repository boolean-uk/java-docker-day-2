package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.response.ErrorResponse;
import com.booleanuk.api.response.PostListResponse;
import com.booleanuk.api.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<PostListResponse> getAllPosts() {
        List<Post> allPosts = this.postRepository.findAll();

        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(allPosts);

        return ResponseEntity.ok(postListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id) {
        Post post = this.postRepository.findById(id).orElse(null);

        if(post == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not find post by id");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        PostResponse postResponse = new PostResponse();
        postResponse.set(post);

        return ResponseEntity.ok(postResponse);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        if(post.getPost() == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not create new post, please check required field.");
            return ResponseEntity.ok(errorResponse);
        }

        Post newPost = this.postRepository.save(post);

        PostResponse postResponse = new PostResponse();
        postResponse.set(newPost);

        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable int id, @RequestBody Post post) {
        if(post.getPost() == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not create new post, please check required field.");
            return ResponseEntity.ok(errorResponse);
        }

        Post postToBeUpdated = this.postRepository.findById(id).orElse(null);

        if(postToBeUpdated == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not find post by id");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        postToBeUpdated.setPost(post.getPost());

        Post updatedPost = this.postRepository.save(postToBeUpdated);

        PostResponse postResponse = new PostResponse();
        postResponse.set(updatedPost);

        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        Post postToBeDeleted = this.postRepository.findById(id).orElse(null);

        if(postToBeDeleted == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not find post by id");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        this.postRepository.deleteById(id);

        PostResponse postResponse = new PostResponse();
        postResponse.set(postToBeDeleted);

        return ResponseEntity.ok(postResponse);
    }
}
