package com.matalban.pollsystem.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String jwtToken;

    private String username;

    public LoginResponse(String username, String jwtToken) {
        this.username = username;
        this.jwtToken = jwtToken;
    }

   }

