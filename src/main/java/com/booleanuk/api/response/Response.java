package com.booleanuk.api.response;

import lombok.Getter;

@Getter
public class Response<T> {
    private String status;
    private T data;

    public Response(String status, T data) {
        this.status = status;
        this.data = data;
    }
}
