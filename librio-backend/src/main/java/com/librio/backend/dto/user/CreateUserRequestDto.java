package com.librio.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequestDto {

    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "Mot de passe obligatoire")
    @Size(min = 4, max = 255)
    private String password;

    public CreateUserRequestDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
