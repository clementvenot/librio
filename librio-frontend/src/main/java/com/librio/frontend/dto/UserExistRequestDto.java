package com.librio.frontend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserExistRequestDto {
	
	 @Email @NotBlank
	 private String email;
	
	 public String getEmail() {
		return email;
	 }
	
	 public void setEmail(String email) {
		this.email = email;
	 }
}

