package com.booleanuk.api.users;

import com.booleanuk.api.exceptions.BadRequestException;
import com.booleanuk.api.exceptions.NotFoundException;
import com.booleanuk.api.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<User> users = userRepository.findAll();
        Response<List<User>> response = new Response<>(
                "success",
                users);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getOne(@PathVariable int id) {
        User user = findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {

        try{
            this.userRepository.save(user);
            Response<User> response = new Response<>(
                    "success",
                    user);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new BadRequestException("bad request");
        }

    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable int id) {
        User userToUpdate = findById(id);
        try{
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setBio(user.getBio());
            this.userRepository.save(user);
            Response<User> response = new Response<>(
                    "success",
                    userToUpdate);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            throw new BadRequestException("bad request");
        }

    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        User user = findById(id);
        this.userRepository.delete(user);

        Response<User> response = new Response<>(
                "success",
                user);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private User findById(int id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found with ID " + id)
        );

    }
}

