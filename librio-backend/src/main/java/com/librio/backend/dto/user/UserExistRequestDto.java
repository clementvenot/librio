package com.librio.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserExistRequestDto {

    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    @Size(max = 255)
    private String email;

    public UserExistRequestDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
