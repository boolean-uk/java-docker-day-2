package com.booleanuk.api.responses;

import java.util.HashMap;
import java.util.Map;


public class SuccessResponse extends Response<Map<String, String>> {

    public SuccessResponse(String message) {
        super();
        this.status = "success";
        Map<String, String> reply = new HashMap<>();
        reply.put("message", message);
        this.data = reply;
    }


}
