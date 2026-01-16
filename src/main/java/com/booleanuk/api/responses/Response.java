package com.booleanuk.api.responses;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class Response<T> {
    protected String status;
    protected T data;

    public void set(T data) {
        this.status = "success";
        this.data = data;
    }
}
