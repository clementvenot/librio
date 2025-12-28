package com.librio.backend.dto.user;

public class UserExistResponseDto {

    private String email;
    private boolean exists;

    public UserExistResponseDto() {}

    public UserExistResponseDto(String email, boolean exists) {
        this.email = email;
        this.exists = exists;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isExists() { return exists; }
    public void setExists(boolean exists) { this.exists = exists; }
}
