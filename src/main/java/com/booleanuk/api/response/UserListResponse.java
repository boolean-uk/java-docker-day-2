package com.booleanuk.api.response;



import com.booleanuk.api.user.User;

import java.util.List;

public class UserListResponse extends Response<List<User>> {

    public UserListResponse(List<User> data) {
        super("success", data);
    }
}