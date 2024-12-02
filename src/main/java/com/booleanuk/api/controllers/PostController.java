package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.payload.response.SuccessResponse;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users/{username}/posts")
    public ResponseEntity<Response<?>> getAllUserPosts(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(user.getPosts()));
    }

    @GetMapping("/users/{username}/posts/{id}")
    public ResponseEntity<Response<?>> getSpecificUserPost(@PathVariable String username, @PathVariable int id) {
        User user = userRepository.findByUsername(username).orElse(null);
        Post post = postRepository.findById(id).orElse(null);

        if(user == null || post == null || !post.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(post));
    }

    @PostMapping("/users/{username}/posts")
    public ResponseEntity<Response<?>> createPost(@PathVariable String username, @RequestBody Post post) {
        User tempUser = userRepository.findByUsername(username).orElse(null);

        if(tempUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        post.setUser(tempUser);

        if(post.getContent() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("bad request"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(postRepository.save(post)));
    }

    @PutMapping("/users/{username}/posts/{id}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable String username, @PathVariable int id, @RequestBody Post post) {
        User user = userRepository.findByUsername(username).orElse(null);
        Post postToUpdate = postRepository.findById(id).orElse(null);

        if(user == null || postToUpdate == null || !postToUpdate.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        if(post.getContent() == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("bad request"));
        }

        postToUpdate.setContent(post.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(postRepository.save(postToUpdate)));

    }


    @DeleteMapping("/users/{username}/posts/{id}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable String username, @PathVariable int id) {
        User user = userRepository.findByUsername(username).orElse(null);
        Post postToDelete = postRepository.findById(id).orElse(null);

        if(user == null || postToDelete == null || !postToDelete.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        postRepository.delete(postToDelete);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(postToDelete));

    }
}
