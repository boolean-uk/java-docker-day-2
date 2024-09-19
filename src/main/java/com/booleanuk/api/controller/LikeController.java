package com.booleanuk.api.controller;

import com.booleanuk.api.models.Like;
import com.booleanuk.api.models.LikeRequest;
import com.booleanuk.api.models.Message;
import com.booleanuk.api.models.User;
import com.booleanuk.api.view.LikeRepository;
import com.booleanuk.api.view.MessageRepository;
import com.booleanuk.api.view.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("likes")
public class LikeController {
	private final LikeRepository likeRepository;
	private final UserRepository userRepository;
	private final MessageRepository messageRepository;

	public LikeController(LikeRepository likeRepository, UserRepository userRepository, MessageRepository messageRepository) {
		this.likeRepository = likeRepository;
		this.userRepository = userRepository;
		this.messageRepository = messageRepository;
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(likeRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getOne(@PathVariable int id) {
		Like like = likeRepository.findById(id).orElse(null);
		if (like == null) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<>(like, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> postOne(@RequestBody LikeRequest likeRequest) {
		User user = userRepository.findById(likeRequest.getUserId()).orElse(null);
		Message message = messageRepository.findById(likeRequest.getMessageId()).orElse(null);

		if (user == null || message == null) {
			return ResponseEntity.badRequest().build();
		}

		Like like = new Like();
		like.setUser(user);
		like.setMessage(message);

		return new ResponseEntity<>(likeRepository.save(like), HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> putOne(@PathVariable int id, @RequestParam int userId, @RequestParam int messageId) {
		Like existingLike = likeRepository.findById(id).orElse(null);
		if (existingLike == null) {
			return ResponseEntity.notFound().build();
		}

		User user = userRepository.findById(userId).orElse(null);
		Message message = messageRepository.findById(messageId).orElse(null);

		if (user == null || message == null) {
			return ResponseEntity.badRequest().build();
		}

		existingLike.setUser(user);
		existingLike.setMessage(message);

		return new ResponseEntity<>(likeRepository.save(existingLike), HttpStatus.CREATED);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteOne(@PathVariable int id) {
		Like like = likeRepository.findById(id).orElse(null);
		if (like == null) {
			return ResponseEntity.notFound().build();
		}

		likeRepository.delete(like);
		return new ResponseEntity<>(like, HttpStatus.OK);
	}
}
