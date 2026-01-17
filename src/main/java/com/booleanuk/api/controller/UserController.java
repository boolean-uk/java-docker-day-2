package com.booleanuk.api.controller;

import com.booleanuk.api.models.Message;
import com.booleanuk.api.models.User;
import com.booleanuk.api.view.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
	private final UserRepository userRepository;


	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping
	public ResponseEntity<?> getAll(){
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getOne(@PathVariable int id){
		User user = userRepository.findById(id).orElse(null);
		if (user == null){
			return ResponseEntity.notFound().build();
		}

		return new ResponseEntity<>(user, HttpStatus.OK);
	}


	public boolean checkIfInvalidUser(User user){
		try {
			return user.getUsername() == null || user.getUsername().isEmpty();
		} catch (Exception e) {
			System.out.println(e);
			return true;
		}
	}



	@PostMapping
	public ResponseEntity<?> postOne(@RequestBody User user){
		if (checkIfInvalidUser(user)){
			return ResponseEntity.badRequest().build();
		}

		return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> putOne(@PathVariable int id, @RequestBody User user){
		// check if find by id
		User idUser = userRepository.findById(id).orElse(null);
		if (idUser == null){
			return ResponseEntity.notFound().build();
		}

		if (checkIfInvalidUser(user)){
			return ResponseEntity.badRequest().build();
		}

		idUser.setUsername(user.getUsername());
//		idUser.setMessages(user.getMessages());

		return new ResponseEntity<>(userRepository.save(idUser), HttpStatus.CREATED);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteOne(@PathVariable int id){
		User idUser = userRepository.findById(id).orElse(null);
		if (idUser == null){
			return ResponseEntity.notFound().build();
		}

		userRepository.delete(idUser);

		return new ResponseEntity<>(idUser, HttpStatus.OK);
	}
}
