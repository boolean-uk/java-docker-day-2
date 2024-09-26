package com.booleanuk.api.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ErrorResponse extends Response<String> {
    public void set(String message) {
        this.status = "error";
        this.data = message;
    }
}