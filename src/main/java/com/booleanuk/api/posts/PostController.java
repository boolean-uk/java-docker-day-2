package com.booleanuk.api.posts;

import com.booleanuk.api.exceptions.BadRequestException;
import com.booleanuk.api.exceptions.NotFoundException;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.users.User;
import com.booleanuk.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Post> posts = postRepository.findAll();
        Response<List<Post>> response = new Response<>(
                "success",
                posts);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getOne(@PathVariable int id) {
        Post post = findById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping("users/{userId}")
    public ResponseEntity<?> create(@RequestBody Post post, @PathVariable int userId) {

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("user not found with ID " + userId)
        );

        try{
            post.setUser(user);
            this.postRepository.save(post);
            Response<Post> response = new Response<>(
                    "success",
                    post);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new BadRequestException("bad request");
        }

    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@RequestBody Post post, @PathVariable int id) {
        Post postToUpdate = findById(id);
        try{
            postToUpdate.setContent(post.getContent());
            this.postRepository.save(post);
            Response<Post> response = new Response<>(
                    "success",
                    postToUpdate);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            throw new BadRequestException("bad request");
        }

    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Post post = findById(id);
        this.postRepository.delete(post);

        Response<Post> response = new Response<>(
                "success",
                post);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private Post findById(int id) {
        return this.postRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found with ID " + id)
        );

    }

}
