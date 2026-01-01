package com.librio.frontend.dto;


public class UpdatePasswordResponseDto {

    private String email;

    private String message;

    public UpdatePasswordResponseDto() {}

    public UpdatePasswordResponseDto(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public String getEmail() { 
        return email; 
    }

    public String getMessage() { 
        return message; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public void setMessage(String message) { 
        this.message = message; 
    }
}
