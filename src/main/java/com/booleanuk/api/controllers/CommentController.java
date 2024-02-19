package com.booleanuk.api.controllers;

import com.booleanuk.api.HelperUtils;
import com.booleanuk.api.models.Comment;
import com.booleanuk.api.repositories.CommentRepository;
import com.booleanuk.api.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllComments() {
        List<Comment> commentList = this.commentRepository.findAll();
        return HelperUtils.okRequest(commentList);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createComment(@RequestBody Comment comment) {
        return HelperUtils.createdRequest(this.commentRepository.save(comment));
    }
}
