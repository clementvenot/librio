package com.librio.frontend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdatePasswordRequestDto {

    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    private String email;

    @NotBlank(message = "Nouveau mot de passe obligatoire")
    @Size(min = 5, max = 128, message = "Le mot de passe doit contenir entre 5 et 128 caractères")
    private String newPassword;

    public UpdatePasswordRequestDto() {}

    public UpdatePasswordRequestDto(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

    public String getEmail() { 
        return email; 
    }

    public String getNewPassword() { 
        return newPassword; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public void setNewPassword(String newPassword) { 
        this.newPassword = newPassword; 
    }
}
