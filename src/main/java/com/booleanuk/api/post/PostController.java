package com.booleanuk.api.post;

import com.booleanuk.api.response.*;
import com.booleanuk.api.response.Error;
import com.booleanuk.api.user.User;
import com.booleanuk.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    LocalDateTime currentTime = LocalDateTime.now();

    @GetMapping("posts")
    public ResponseEntity<Response> getAll() {
        return new ResponseEntity<>(new PostListResponse(this.postRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("{userId}/posts/{postId}")
    public ResponseEntity<Response> getPost(@PathVariable int userId, @PathVariable int postId) {
        User user = this.userRepository
                .findById(userId)
                .orElse(null);
        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("User not found")), HttpStatus.NOT_FOUND);
        }

        Post post = this.postRepository
                .findById(postId)
                .orElse(null);
        if (post == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("Post not found")), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new PostResponse(post), HttpStatus.OK);
    }

    @GetMapping("{userId}/posts")
    public ResponseEntity<Response> getPostsToOneUser(@PathVariable int userId) {
        User user = this.userRepository
                .findById(userId)
                .orElse(null);
        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("User not found")), HttpStatus.NOT_FOUND);
        }

        List<Post> posts = postRepository.findByUser(user);
        if (posts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No posts found");
        }
        return new ResponseEntity<>(new PostListResponse(posts), HttpStatus.OK);
    }

    @PostMapping("{userId}/posts")
    public ResponseEntity<Response> createPost(@PathVariable int userId , @RequestBody Post post) {

        User user = this.userRepository
                .findById(userId)
                .orElse(null);
        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("User not found")), HttpStatus.NOT_FOUND);
        }

        if(post.getText().isEmpty() ){
            return new ResponseEntity<>(new ErrorResponse(new Error("Bad Request")), HttpStatus.BAD_REQUEST);
        }

        post.setCreatedAt(currentTime);
        post.setUpdatedAt(currentTime);
        post.setUser(user);
        this.postRepository.save(post);
        return new ResponseEntity<>(new PostResponse(post), HttpStatus.CREATED);
    }

    @DeleteMapping("{userId}/posts/{postId}")
    public ResponseEntity<Response> deletePost (@PathVariable int userId, @PathVariable int postId) {

        User user = this.userRepository
                .findById(userId)
                .orElse(null);
        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("User not found")), HttpStatus.NOT_FOUND);
        }

        Post deleted = this.postRepository
                .findById(postId)
                .orElse(null);

        if (deleted == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("Post not found")), HttpStatus.NOT_FOUND);
        }

        this.postRepository.delete(deleted);
        return new ResponseEntity<>(new PostResponse(deleted), HttpStatus.OK);
    }

    @PutMapping("{userId}/posts/{postId}")
    public ResponseEntity<Response> updateCourse (@PathVariable int userId, @PathVariable int postId, @RequestBody Post post) {

        User user = this.userRepository
                .findById(userId)
                .orElse(null);
        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("User not found")), HttpStatus.NOT_FOUND);
        }

        Post postToUpdate = this.postRepository
                .findById(postId)
                .orElse(null);
        if(postToUpdate == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("Post not found")), HttpStatus.NOT_FOUND);
        }

        if(post.getText().isEmpty()){
            return new ResponseEntity<>(new ErrorResponse(new Error("Bad Request")), HttpStatus.BAD_REQUEST);
        }

        postToUpdate.setText(post.getText());
        post.setUpdatedAt(currentTime);
        this.postRepository.save(postToUpdate);
        return new ResponseEntity<>(new PostResponse(postToUpdate), HttpStatus.OK);
    }
}
