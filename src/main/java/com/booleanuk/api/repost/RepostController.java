package com.booleanuk.api.repost;

import com.booleanuk.api.exceptions.BadRequestException;
import com.booleanuk.api.exceptions.NotFoundException;
import com.booleanuk.api.posts.Post;
import com.booleanuk.api.posts.PostRepository;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.users.User;
import com.booleanuk.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/posts/{postId}/repost")
public class RepostController {

    @Autowired
    RepostRepository repostRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @PostMapping()
    public ResponseEntity<?> repostPost(@PathVariable int userId, @PathVariable int postId) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("user not found with ID " + userId)
        );

        Post post = this.postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("post not found with ID " + postId)
        );
        Repost repost = new Repost();
        repost.setPost(post);
        repost.setUser(user);
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);

        Response<Repost> response = new Response<>(
                "Success",repost
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<?> delete( @PathVariable int id, @PathVariable int userId, @PathVariable int postId) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("No User with that id: " + userId + " found")
        );

        Post post = this.postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("No Post with that id: " + postId + " found")
        );

        Repost repost = this.repostRepository.findById(id).orElseThrow(
                () -> new NotFoundException("No repost not found with ID " + id)
        );
        try {
            post.setLikesCount(post.getLikesCount() - 1);
            repostRepository.delete(repost);
            Response<Repost> response = new Response<>(
                    "success",
                    repost);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new BadRequestException("bad request");
        }


    }
}
