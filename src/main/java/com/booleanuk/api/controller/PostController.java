package com.booleanuk.api.controller;

import com.booleanuk.api.model.ApiResponse;
import com.booleanuk.api.model.Like;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.LikeRepository;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("post")
public class PostController {
    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final LikeRepository likeRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    @PostMapping("{userId}")
    public ResponseEntity<ApiResponse<?>> createPost (@PathVariable int userId, @RequestBody Post postDetails) {
        Optional<User> use = this.userRepository.findById(userId);

        if(use.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find user with id " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        User user = use.get();

        if(postDetails.isInvalid()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not create a post with the specified parameters.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Post post = new Post(postDetails.getContents(), user, null);
        user.getPosts().add(post);

        this.postRepository.save(post);
        this.userRepository.save(user);

        ApiResponse<Post> response = new ApiResponse<>("success", post);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("{userId}/{postId}")
    public ResponseEntity<ApiResponse<?>> createComment (@PathVariable int userId, @PathVariable int postId, @RequestBody Post postDetails) {
        Optional<User> use = this.userRepository.findById(userId);

        if(use.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find user with id " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        User user = use.get();

        Optional<Post> pos = this.postRepository.findById(postId);
        if(pos.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find post with id " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Post post = pos.get();

        if(postDetails.isInvalid()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not create a post with the specified parameters.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Post comment = new Post(postDetails.getContents(), user, post);

        user.getPosts().add(comment);
        post.getComments().add(comment);

        this.postRepository.save(comment);
        this.postRepository.save(post);
        this.userRepository.save(user);

        ApiResponse<Post> response = new ApiResponse<>("success", comment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("{postId}/like/{userId}")
    public ResponseEntity<ApiResponse<?>> createLike (@PathVariable int userId, @PathVariable int postId) {
        Optional<User> use = this.userRepository.findById(userId);
        if(use.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find user with id " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        User user = use.get();

        Optional<Post> pos = this.postRepository.findById(postId);
        if(pos.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find post with id " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Post post = pos.get();

        Like like = new Like(user, post);
        user.getLikes().add(like);
        post.getLikes().add(like);

        this.likeRepository.save(like);
        this.postRepository.save(post);
        this.userRepository.save(user);

        ApiResponse<Like> response = new ApiResponse<>("success", like);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("{postId}/repost/{userId}")
    public ResponseEntity<ApiResponse<?>> repost (@PathVariable int userId, @PathVariable int postId) {
        Optional<User> use = this.userRepository.findById(userId);
        if(use.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find user with id " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        User user = use.get();

        Optional<Post> pos = this.postRepository.findById(postId);
        if(pos.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find post with id " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Post post = pos.get();

        user.getPosts().add(post);
        this.userRepository.save(user);

        ApiResponse<Post> response = new ApiResponse<>("success", post);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Post>>> readAll() {
        List<Post> posts = this.postRepository.findAll();
        ApiResponse<List<Post>> response = new ApiResponse<>("success", posts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<?>> readOne (@PathVariable int id) {
        Optional<Post> pos = this.postRepository.findById(id);

        if(pos.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find post with id " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Post post = pos.get();
        ApiResponse<Post> response = new ApiResponse<>("success", post);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<?>> update (@PathVariable int id, @RequestBody Post postDetails) {
        Optional<Post> pos = this.postRepository.findById(id);

        if(pos.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find post with id " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Post post = pos.get();

        if(postDetails.isInvalid()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not create a post with the specified parameters.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        post.setContents(postDetails.getContents());

        ApiResponse<Post> response = new ApiResponse<>("success", post);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> delete (@PathVariable int id) {
        Optional<Post> pos = this.postRepository.findById(id);

        if(pos.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find post with id " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Post post = pos.get();
        this.postRepository.delete(post);

        ApiResponse<Post> response = new ApiResponse<>("success", post);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("like/{id}")
    public ResponseEntity<ApiResponse<?>> deleteLike (@PathVariable int id) {
        Optional<Like> lik = this.likeRepository.findById(id);

        if(lik.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not find like with id " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Like like = lik.get();

        ApiResponse<Like> response = new ApiResponse<>("success", like);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
