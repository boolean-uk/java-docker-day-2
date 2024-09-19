package com.booleanuk.api.controller;

import com.booleanuk.api.models.Message;
import com.booleanuk.api.view.MessageRepository;
import com.booleanuk.api.view.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("messages")
public class MessageController {
	private final MessageRepository messageRepository;
	private final UserRepository userRepository;

	public MessageController(MessageRepository messageRepository, UserRepository userRepository) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
	}

	@GetMapping
	public ResponseEntity<?> getAll(){
		return new ResponseEntity<>(messageRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getOne(@PathVariable int id){
		Message message = messageRepository.findById(id).orElse(null);
		if (message == null){
			return ResponseEntity.notFound().build();
		}

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	public boolean checkIfInvalidMessage(Message message){
		try{
			return message.getText() != null && userRepository.existsById(message.getUser().getId());
		} catch (Exception e) {
			return false;
		}
	}

	@PostMapping
	public ResponseEntity<?> postOne(@RequestBody Message message){
		if (checkIfInvalidMessage(message)){
			return ResponseEntity.badRequest().build();
		}

		return new ResponseEntity<>(messageRepository.save(message), HttpStatus.CREATED);

	}

	@PutMapping("{id}")
	public ResponseEntity<?> putOne(@PathVariable int id, @RequestBody Message message){
		// check if find by id
		Message idMessage = messageRepository.findById(id).orElse(null);
		if (idMessage == null){
			return ResponseEntity.notFound().build();
		}

		if (checkIfInvalidMessage(message)){
			return ResponseEntity.badRequest().build();
		}

		idMessage.setText(message.getText());
		idMessage.setUser(message.getUser());
		idMessage.setPostedDate(message.getPostedDate());


		return new ResponseEntity<>(messageRepository.save(idMessage), HttpStatus.CREATED);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteOne(@PathVariable int id){
		Message idMessage = messageRepository.findById(id).orElse(null);
		if (idMessage == null){
			return ResponseEntity.notFound().build();
		}

		messageRepository.delete(idMessage);

		return new ResponseEntity<>(idMessage, HttpStatus.OK);
	}




}
