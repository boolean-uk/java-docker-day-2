package com.booleanuk.api.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse extends Response<String>{
    public void set(String message) {
        this.status = "error";
        this.data = message;
    }
}
