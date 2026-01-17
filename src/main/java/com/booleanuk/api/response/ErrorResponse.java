package com.booleanuk.api.response;

import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse extends Response<Map<String, String>>{

    public ErrorResponse(String status, Map<String, String> data) {
        super(status, data);
    }
}
