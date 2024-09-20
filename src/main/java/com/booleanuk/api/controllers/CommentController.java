package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Comment;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.repositories.CommentRepository;
import com.booleanuk.api.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("posts/{postId}/comments")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody Comment comment, @PathVariable int postId){
        Post post = this.postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No post with that id")
        );
        comment.setPost(post);
        return new ResponseEntity<>(this.commentRepository.save(comment), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Comment> getAll(@PathVariable int postId){
        this.postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No post with that id")
        );
        return this.commentRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Comment> getOne(@PathVariable int postId, @PathVariable int id){
        this.postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No post with that id")
        );
        Comment comment = this.commentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No comment with that id")
        );
        return ResponseEntity.ok(comment);
    }

    @PutMapping("{id}")
    public ResponseEntity<Comment> update(@RequestBody Comment comment, @PathVariable int postId, @PathVariable int id){
        this.postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No post with that id")
        );
        Comment commentToUpdate = this.commentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No comment with that id")
        );
        commentToUpdate.setContent(comment.getContent());
        return new ResponseEntity<>(this.commentRepository.save(commentToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Comment> delete(@PathVariable int postId, @PathVariable int id){
        Comment comment = this.commentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No comment with that id")
        );
        this.commentRepository.delete(comment);
        return ResponseEntity.ok(comment);
    }
}
