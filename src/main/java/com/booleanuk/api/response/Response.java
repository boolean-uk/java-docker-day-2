package com.booleanuk.api.response;

public class Response<T> {
    private String status;
    private T data;

    public Response(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

}