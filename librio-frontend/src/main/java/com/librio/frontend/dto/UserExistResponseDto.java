package com.librio.frontend.dto;

public class UserExistResponseDto {
	
    private String email;
    private boolean exists;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isExists() {
		return exists;
	}
	public void setExists(boolean exists) {
		this.exists = exists;
	}
}
