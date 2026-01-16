package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.model.dto.FriendListDTO;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("friendlists")
public class FriendListController {

    @Autowired
    private UserRepository users;

    @PostMapping
    public ResponseEntity<Response<?>> addToFriendList(@RequestBody FriendListDTO request) {
        User user = this.users.findById(request.getUser()).orElse(null);
        User userToFriend = this.users.findById(request.getUserToAdd()).orElse(null);

        if (user == null || userToFriend == null) {
            ErrorResponse errorResponse = new ErrorResponse("user(s) not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        user.addFriend(userToFriend);
        userToFriend.addFriend(user);

        this.users.save(user);
        this.users.save(userToFriend);

        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

}
