package com.booleanuk.api.controller;

import com.booleanuk.api.model.Comment;
import com.booleanuk.api.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository comments;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        Comment savedComment = comments.save(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int id) {
        Optional<Comment> optionalComment = comments.findById(id);
        return optionalComment.map(comment -> new ResponseEntity<>(comment, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable int id, @RequestBody Comment comment) {
        Optional<Comment> optionalExistingComment = comments.findById(id);
        if (!optionalExistingComment.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Comment existingComment = optionalExistingComment.get();
        comment.setId(id);
        comment.setCreatedAt(existingComment.getCreatedAt());
        comment.setUpdatedAt(LocalDateTime.now());
        Comment updatedComment = comments.save(comment);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable int id) {
        Optional<Comment> optionalComment = comments.findById(id);
        if (optionalComment.isPresent()) {
            Comment deletedComment = optionalComment.get();
            comments.deleteById(id);
            return new ResponseEntity<>(deletedComment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
