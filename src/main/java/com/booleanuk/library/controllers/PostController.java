package com.booleanuk.library.controllers;

import com.booleanuk.library.models.Post;
import com.booleanuk.library.payload.response.ErrorResponse;
import com.booleanuk.library.payload.response.PostListResponse;
import com.booleanuk.library.payload.response.PostResponse;
import com.booleanuk.library.payload.response.Response;
import com.booleanuk.library.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostRepository loanRepository;

    @GetMapping
    public ResponseEntity<PostListResponse> getAllLoans() {
        PostListResponse response = new PostListResponse();
        response.set(loanRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getLoanById(@PathVariable int id) {
        Post loan = loanRepository.findById(id).orElse(null);
        if (loan == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Loan not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        PostResponse loanResponse = new PostResponse();
        loanResponse.set(loan);
        return ResponseEntity.ok(loanResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createLoan(@RequestBody Post loan) {
        PostResponse loanResponse = new PostResponse();
        try {
            loanResponse.set(loanRepository.save(loan));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(loanResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateLoan(@PathVariable int id, @RequestBody Post loanDetails) {
        Post loan = loanRepository.findById(id).orElse(null);
        if (loan == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Loan not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Update post details
        loan.setUser(loanDetails.getUser());
        loan.setBlog(loanDetails.getBlog());
        loan.setTitle(loanDetails.getTitle());
        loan.setContent(loanDetails.getContent());
        loan.setLikesCount(loanDetails.getLikesCount());
        PostResponse loanResponse = new PostResponse();
        try {
            loanResponse.set(loanRepository.save(loan));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(loanResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteLoan(@PathVariable int id) {
        Post loan = loanRepository.findById(id).orElse(null);
        if (loan == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Loan not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        loanRepository.delete(loan);
        PostResponse loanResponse = new PostResponse();
        loanResponse.set(loan);
        return ResponseEntity.ok(loanResponse);
    }
}