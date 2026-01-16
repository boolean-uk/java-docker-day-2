package com.booleanuk.api.controller;

import com.booleanuk.api.model.Comment;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.CommentRepository;
import com.booleanuk.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("comments")
public class CommentController {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @PostMapping
    public ResponseEntity<Comment> create(@RequestParam int postId, @RequestBody Comment body) {
        Optional<Post> existingPost = this.postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post ID not found");
        }
        body.setPost(existingPost.get());
        Comment newComment = this.commentRepository.save(body);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAll() {
        return ResponseEntity.ok(this.commentRepository.findAll());
    }

    @PutMapping
    public ResponseEntity<Comment> update(@RequestParam int commentId, @RequestBody Comment body) {
        Optional<Comment> existingComment = this.commentRepository.findById(commentId);
        if (existingComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment ID not found");
        }
        Comment commentToUpdate = existingComment.get();
        commentToUpdate.setContent(body.getContent());
        return new ResponseEntity<>(this.commentRepository.save(commentToUpdate), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Comment> delete(@RequestParam int commentId) {
        return this.commentRepository.findById(commentId)
                .map(comment -> {
                    this.commentRepository.delete(comment);
                    return new ResponseEntity<>(comment, HttpStatus.OK);

                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    }
}
