package com.booleanuk.api.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response<T> {
    protected String status;
    protected T data;

    public void set(T data) {
        this.status = "success";
        this.data = data;
    }
}
