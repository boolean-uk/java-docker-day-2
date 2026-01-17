package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.ErrorMessage;
import com.booleanuk.api.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PostRepository postRepository;

	@GetMapping
	public ResponseEntity<Response<List<User>>> getAll() {
		Response<List<User>> response = new Response<>("success", userRepository.findAll());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<?>> getById(@PathVariable int id) {
		User user = userRepository.findById(id).orElse(null);
		if (user != null) {
			Response<User> response = new Response<>("success", user);
			return ResponseEntity.ok(response);
		} else {
			Response<ErrorMessage> response = new Response<>("error", new ErrorMessage("not found"));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Response<?>> create(@RequestBody User user) {
		User savedUser = userRepository.save(user);
		Response<User> response = new Response<>("success", savedUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody User updatedUser) {
		User user = userRepository.findById(id).orElse(null);
		if (user != null) {
			if (updatedUser.getUsername() != null) {
				user.setUsername(updatedUser.getUsername());
			}
			User savedUser = userRepository.save(user);
			Response<User> response = new Response<>("success", savedUser);
			return ResponseEntity.ok(response);
		} else {
			Response<ErrorMessage> response = new Response<>("error", new ErrorMessage("not found"));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		User user = userRepository.findById(id).orElse(null);
		if (user!=null) {
			userRepository.deleteById(id);
			Response<User> response = new Response<>("success", user);
			return ResponseEntity.ok(response);
		} else {
			Response<ErrorMessage> response = new Response<>("error", new ErrorMessage("not found"));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);		}
	}
}
