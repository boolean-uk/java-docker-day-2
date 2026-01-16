package com.booleanuk.api.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {

    private String status;
    private T data;

    public Response(String message, T data) {
        this.status = message;
        this.data = data;
    }
}
