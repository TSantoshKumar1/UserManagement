package com.service.demo.dto;

public class EmailDto {
    private String token;
    private String email;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EmailDto{" +
                "token='" + token + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
