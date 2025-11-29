package com.booleanuk.api.controller;

import com.booleanuk.api.dto.RepostDTO;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.Repost;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.RepostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reposts")
public class RepostController {

    @Autowired
    RepostRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping()
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.repository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Repost repost = this.repository.findById(id).orElse(null);

        if(repost == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(repost);
    }

    @PostMapping()
    public ResponseEntity<?> repost(@RequestBody RepostDTO repostDTO) {
        User user = this.userRepository.findById(repostDTO.getUser()).orElse(null);
        Post post = this.postRepository.findById(repostDTO.getPost()).orElse(null);

        if(user == null || post == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Repost repost = new Repost(user, post);

        this.repository.save(repost);

        return ResponseEntity.ok(repost);
    }
}
