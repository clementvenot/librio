package com.librio.backend.dto.user;

public class LoginResponseDto {

    private boolean authenticated;
    private String message;

    public LoginResponseDto() {}

    public LoginResponseDto(boolean authenticated, String message) {
        this.authenticated = authenticated;
        this.message = message;
    }

    public boolean isAuthenticated() { return authenticated; }
    public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
