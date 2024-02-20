package com.booleanuk.api.payload.response;

public class SuccessResponse<T> extends Response<T>{

    public SuccessResponse(T data) {
        super("success", data);
    }
}
