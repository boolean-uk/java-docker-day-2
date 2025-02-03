package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Upvote;
import com.booleanuk.api.models.UpvoteId;
import com.booleanuk.api.repositories.UpvoteRepository;
import com.booleanuk.api.response.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/upvotes")
public class UpvoteController {

    @Autowired
    private UpvoteRepository upvoteRepository;

    private final Response<Object> response = new Response<>();

    @GetMapping
    public ResponseEntity<?> getAll() {
        response.setSuccess(upvoteRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/{userId}")
    public ResponseEntity<?> getById(@PathVariable int postId, @PathVariable int userId) {
        UpvoteId id = new UpvoteId(postId, userId);

        Optional<Upvote> upvote = upvoteRepository.findById(id);
        if (upvote.isEmpty()) {
            response.setError("not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.setSuccess(upvote.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Upvote upvote) {
        UpvoteId id = new UpvoteId(upvote.getPost().getId(), upvote.getUser().getId());
        upvote.setId(id);
        System.out.println(upvote);

        try {
            Upvote newUpvote = upvoteRepository.save(upvote);
            response.setSuccess(newUpvote);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setError("Bad request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{postId}/{userId}")
    public ResponseEntity<?> update(@PathVariable int postId, @PathVariable int userId, @RequestBody Upvote updatedUpvote) {
        UpvoteId id = new UpvoteId(postId, userId);

        Optional<Upvote> existingUpvoteOptional = upvoteRepository.findById(id);
        if (existingUpvoteOptional.isEmpty()) {
            response.setError("not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Upvote existingUpvote = existingUpvoteOptional.get();
        BeanUtils.copyProperties(updatedUpvote, existingUpvote, "id");

        try {
            Upvote updatedEntity = upvoteRepository.save(existingUpvote);
            response.setSuccess(updatedEntity);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setError("Bad request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<?> delete(@PathVariable int postId, @PathVariable int userId) {
        UpvoteId id = new UpvoteId(postId, userId);

        Upvote upvoteToDelete = upvoteRepository.findById(id).orElse(null);
        if (upvoteToDelete == null) {
            response.setError("not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            upvoteRepository.deleteById(id);
            response.setSuccess(upvoteToDelete);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setError("Bad request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
