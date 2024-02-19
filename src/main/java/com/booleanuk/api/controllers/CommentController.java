package com.booleanuk.api.controllers;

import com.booleanuk.api.HelperUtils;
import com.booleanuk.api.models.Comment;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.CommentRepository;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllComments() {
        List<Comment> commentList = this.commentRepository.findAll();
        return HelperUtils.okRequest(commentList);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createComment(@RequestBody Comment comment) {
        Optional<User> userOptional = this.userRepository.findById(comment.getUser().getId());
        Optional<Post> postOptional = this.postRepository.findById(comment.getPost().getId());

        if (userOptional.isPresent() && postOptional.isPresent()) {
           User user = userOptional.get();
           Post post = postOptional.get();

           comment.setUser(user);
           comment.setPost(post);

           Comment savedComment = this.commentRepository.save(comment);

           return HelperUtils.createdRequest(savedComment);
        } else {
            return HelperUtils.badRequest(new ApiResponse.Message("no"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable int id) {
        Optional<Comment> commentOptional = this.commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            this.commentRepository.deleteById(id);
            return HelperUtils.okRequest(commentOptional);
        } else {
            return HelperUtils.badRequest(new ApiResponse.Message("NO"));
        }
    }
}
