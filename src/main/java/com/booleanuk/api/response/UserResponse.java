package com.booleanuk.api.response;

import com.booleanuk.api.model.User;

public class UserResponse extends Response<User> {
    private User data;

    public UserResponse(User user) {
        super("success", user);
    }
}
