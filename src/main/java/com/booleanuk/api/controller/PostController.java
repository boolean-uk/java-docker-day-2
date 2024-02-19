package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.response.ErrorMessage;
import com.booleanuk.api.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

	@Autowired
	PostRepository postRepository;

	@GetMapping
	public ResponseEntity<Response<List<Post>>> getAll() {
		List<Post> posts = postRepository.findAll();
		Response<List<Post>> response = new Response<>("success", posts);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<?>> getById(@PathVariable int id) {
		Post post = postRepository.findById(id).orElse(null);
		if (post != null) {
			Response<Post> response = new Response<>("success", post);
			return ResponseEntity.ok(response);
		} else {
			Response<ErrorMessage> response = new Response<>("error", new ErrorMessage("not found"));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Response<?>> create(@RequestBody Post post) {
		Post savedPost = postRepository.save(post);
		Response<Post> response = new Response<>("success", savedPost);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody Post updatedPost) {
		Post post = postRepository.findById(id).orElse(null);
		if (post != null) {
			if (updatedPost.getContent() != null) {
				post.setContent(updatedPost.getContent());
			}
			Post savedPost = postRepository.save(post);
			Response<Post> response = new Response<>("success", savedPost);
			return ResponseEntity.ok(response);
		} else {
			Response<ErrorMessage> response = new Response<>("error", new ErrorMessage("not found"));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		Post post = postRepository.findById(id).orElse(null);
		if (post != null) {
			postRepository.deleteById(id);
			Response<Post> response = new Response<>("success", post);
			return ResponseEntity.ok(response);
		} else {
			Response<ErrorMessage> response = new Response<>("error", new ErrorMessage("not found"));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
}
