package com.booleanuk.api.controllers;
import com.booleanuk.api.models.Comment;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.CommentRepository;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/posts")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable int postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return ResponseEntity.ok(commentRepository.findByPostId(post.getId()));
    }
    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }
    @PostMapping("/{postId}/comments/create/{userId}")
    public ResponseEntity<?> createComment(@PathVariable int postId, @PathVariable int userId, @RequestBody Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if(comment.getContent() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        comment.setUser(user);
        comment.setCreated(java.time.OffsetDateTime.now());
        comment.setPost(post);
        commentRepository.save(comment);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
    @PutMapping("/{postId}/comments/{commentId}/update")
    public ResponseEntity<?> updateComment(@PathVariable int commentId, @RequestBody Comment comment) {
        Comment commentToUpdate = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        if(comment.getContent() != null){
            commentToUpdate.setContent(comment.getContent());
        }
        commentRepository.save(commentToUpdate);
        return new ResponseEntity<>(commentToUpdate, HttpStatus.OK);
    }
    @DeleteMapping("/{postId}/comments/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        commentRepository.delete(comment);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }



}
