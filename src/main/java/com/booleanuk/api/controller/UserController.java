package com.booleanuk.api.controller;

import com.booleanuk.api.dto.UserDTO;
import com.booleanuk.api.dto.UserMapper;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    UserRepository userRepository;
    UserMapper userMapper;

    public UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllPosts() {
        List<UserDTO> userList = new ArrayList<>();

        this.userRepository
                .findAll()
                .forEach(user ->
                        userList.add(userMapper.toDTO(user)));

        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    UserDTO userDTO = userMapper.toDTO(user);
                    return ResponseEntity.ok(userDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // TODO: Change this to get user info based on token
    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User savedUser = this.userRepository.save(userMapper.toEntity(userDTO));
        return new ResponseEntity<>(this.userMapper.toDTO(savedUser), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Integer id, @RequestBody UserDTO updatedUserDTO) {
        return userRepository.findById(id)
                .map(userToUpdate -> {
                    userToUpdate.setUsername(updatedUserDTO.getUsername());
                    userToUpdate.setEmail(updatedUserDTO.getEmail());
                    userRepository.save(userToUpdate);
                    return new ResponseEntity<>(userMapper.toDTO(userToUpdate), HttpStatus.CREATED);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deletePost(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(userToDelete -> {
                    userRepository.delete(userToDelete);
                    return ResponseEntity.ok(userMapper.toDTO(userToDelete));
                }).orElse(ResponseEntity.notFound().build());
    }
}
