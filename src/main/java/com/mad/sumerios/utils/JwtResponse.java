package com.mad.sumerios.utils;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type;
    private String username;

    public JwtResponse(String token, String username) {
        this.token = token;
        this.type = "Bearer";  // Fijar el tipo como 'Bearer'
        this.username = username;
    }
}


