package com.booleanuk.api.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type;
    private int id;
    private String username;
    private String email;

    public JwtResponse(String token, int id, String username, String email) {
        this.token = token;
        this.type = "Bearer";
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
