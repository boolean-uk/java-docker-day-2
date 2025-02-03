package com.booleanuk.api.controller;

import com.booleanuk.api.model.Comment;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.CommentRepository;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    private ErrorResponse errorResponse = new ErrorResponse();
    private PostResponse postResponse = new PostResponse();
    private PostListResponse postListResponse = new PostListResponse();
    private CommentListResponse commentListResponse = new CommentListResponse();
    private CommentResponse commentResponse = new CommentResponse();

    @GetMapping
    public ResponseEntity<Response<?>> getAllPosts() {
        postListResponse.set(this.postRepository.findAll());
        return ResponseEntity.ok(postListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createPost(@RequestBody Post post) {
        post.setPosted(LocalDateTime.now());
        post.setUpdated(LocalDateTime.now());
        try {
            postResponse.set(this.postRepository.save(post));
        } catch (Exception e) {
            errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getOnePost(@PathVariable int id) {
        Post post = this.postRepository.findById(id).orElse(null);
        if (post == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.postResponse.set(post);
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable int id, @RequestBody Post post) {
        Post postToUpdate = this.postRepository.findById(id).orElse(null);
        if (postToUpdate == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        postToUpdate.setTitle(post.getTitle());
        postToUpdate.setContent(post.getContent());
        postToUpdate.setUpdated(LocalDateTime.now());
        try {
            postToUpdate = this.postRepository.save(postToUpdate);
        } catch (Exception e) {
            this.errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        this.postResponse.set(postToUpdate);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int id) {
        Post postToDelete = this.postRepository.findById(id).orElse(null);
        if (postToDelete == null) {
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.postRepository.delete(postToDelete);
        postResponse.set(postToDelete);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("{id}/comments")
    public ResponseEntity<Response<?>> getAllComments(@PathVariable int id) {
        Post postWithComments = this.postRepository.findById(id).orElse(null);
        if (postWithComments == null) {
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.commentListResponse.set(postWithComments.getComments());
        return ResponseEntity.ok(commentListResponse);
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<Response<?>> addComment(@PathVariable int id, @RequestBody Comment comment) {
        Post postWithComments = this.postRepository.findById(id).orElse(null);
        if (postWithComments == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        postWithComments.getComments().add(comment);
        this.commentResponse.set(comment);
        return ResponseEntity.ok(commentResponse);
    }

    @GetMapping("{post_id}/comments/{comment_id}")
    public ResponseEntity<Response<?>> getOneComment(@PathVariable int postId, @PathVariable int commentId) {
        Post post = this.postRepository.findById(postId).orElse(null);
        Comment comment = this.commentRepository.findById(commentId).orElse(null);
        if (post == null || comment == null) {
            this.errorResponse.set("Post or comment not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.commentResponse.set(comment);
        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("{post_id}/friends/{comment_id}")
    public ResponseEntity<Response<?>> removeComment(@PathVariable int postId, @PathVariable int commentId) {
        Post post = this.postRepository.findById(postId).orElse(null);
        Comment comment = this.commentRepository.findById(commentId).orElse(null);
        if (post == null || comment == null) {
            this.errorResponse.set("Post or comment not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        post.getComments().remove(comment);
        this.commentResponse.set(comment);
        return ResponseEntity.ok(commentResponse);
    }
}
