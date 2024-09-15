package com.booleanuk.api.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ErrorResponse extends Response<String>{
    @Override
    public void set(String message){
        this.status = "error";
        this.data = message;
    }
}