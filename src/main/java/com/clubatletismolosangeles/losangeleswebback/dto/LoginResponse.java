package com.clubatletismolosangeles.losangeleswebback.dto;

import java.util.List;

public class LoginResponse {
    public String jwt;
    public List<String> roles;

    public LoginResponse(String jwt, List<String> roles) {
        this.jwt = jwt;
        this.roles = roles;
    }
}