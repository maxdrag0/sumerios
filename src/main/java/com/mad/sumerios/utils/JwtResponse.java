package com.mad.sumerios.utils;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type;
    private String username;

    public JwtResponse(String token, String username,String tokenType) {
        this.token = token;
        this.type = tokenType;
        this.username = username;
    }
}


