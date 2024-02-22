package com.booleanuk.library.controllers;

import com.booleanuk.library.models.Blog;
import com.booleanuk.library.payload.response.ErrorResponse;
import com.booleanuk.library.payload.response.BlogListResponse;
import com.booleanuk.library.payload.response.BlogResponse;
import com.booleanuk.library.payload.response.Response;
import com.booleanuk.library.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogRepository itemRepository;

    @GetMapping
    public ResponseEntity<BlogListResponse> getAllItems() {
        BlogListResponse response = new BlogListResponse();
        response.set(itemRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getItemById(@PathVariable int id) {
        Blog item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Item not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        BlogResponse itemResponse = new BlogResponse();
        itemResponse.set(item);
        return ResponseEntity.ok(itemResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createItem(@RequestBody Blog item) {
        BlogResponse itemResponse = new BlogResponse();
        try {
            itemResponse.set(itemRepository.save(item));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(itemResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateItem(@PathVariable int id, @RequestBody Blog itemDetails) {
        Blog item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Item not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        // Update item details
        item.setName(itemDetails.getName());
        item.setType(itemDetails.getType());
        item.setGenreCategory(itemDetails.getGenreCategory());
        BlogResponse itemResponse = new BlogResponse();
        try {
            itemResponse.set(itemRepository.save(item));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(itemResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteItem(@PathVariable int id) {
        Blog item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Item not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        itemRepository.delete(item);
        BlogResponse itemResponse = new BlogResponse();
        itemResponse.set(item);
        return ResponseEntity.ok(itemResponse);
    }
}
