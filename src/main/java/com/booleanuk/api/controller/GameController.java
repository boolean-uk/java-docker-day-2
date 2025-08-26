package com.booleanuk.api.controller;

import com.booleanuk.api.model.Game;
import com.booleanuk.api.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;


    @GetMapping
    public ResponseEntity<List<Game>> getAll(){
        return ResponseEntity.ok(this.gameRepository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Game> get(@PathVariable int id){
        Game employee = this.gameRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This is not found by dave!"));
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<Game> add(@RequestBody Game employee){
        return new ResponseEntity<>(this.gameRepository.save(employee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> update( @PathVariable int id,@RequestBody Game employee){
        Game empToUpdate = this.gameRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This is not found by dave!"));
        empToUpdate.setTitle(employee.getTitle());
        empToUpdate.setGenre(employee.getGenre());
        empToUpdate.setPublisher(employee.getPublisher());
        empToUpdate.setDeveloper(employee.getDeveloper());
        empToUpdate.setReleaseYear(employee.getReleaseYear());
        empToUpdate.setIsEarlyAccess(employee.getIsEarlyAccess());

        return new ResponseEntity<>(this.gameRepository.save(empToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Game> delete(@PathVariable int id){
        Game empToBeDeleted = this.gameRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This is not found by dave!"));
        this.gameRepository.delete(empToBeDeleted);
        return ResponseEntity.ok(empToBeDeleted);
    }

    
//    @GetMapping("{id}")
//    public Game getById(@PathVariable("id") Integer id) {
//        return this.gameRepository.findById(id).orElseThrow();
//    }
//
//    record PostGame(String email, String genre) {}
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public Game create(@RequestBody PostGame request) {
//        Game user = new Game(request.email(), request.genre());
//        return this.gameRepository.save(user);
//    }
}
