package com.booleanuk.api.response;

import com.booleanuk.api.model.User;
import java.util.List;

public class UserListResponse extends Response<List<User>> {
    private List<User> data;
    public UserListResponse(List<User> data) {
        super("success", data);
    }
}
