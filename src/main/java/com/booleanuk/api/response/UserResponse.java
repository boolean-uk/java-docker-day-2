package com.booleanuk.api.response;

import com.booleanuk.api.user.User;


public class UserResponse extends Response<User>{

    public UserResponse(User data) {
        super("success", data);
    }
}

