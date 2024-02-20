package com.booleanuk.api.payload.response;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse extends Response<Map<String, String>> {

    public void set(String message) {
        this.status = "Error";
        this.message = new HashMap<>(){{
            put("message", message);
        }};
    }
}
