package com.librio.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "UpdatePasswordRequestDto",
    description = "Donnée pour mettre à jour uniquement le mot de passe de l'utilisateur identifié par email."
)
public class UpdatePasswordRequestDto {

    @Schema(
        description = "Email de l'utilisateur dont on souhaite changer le mot de passe",
        example = "user@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    private String email;

    @Schema(
        description = "Nouveau mot de passe en clair (pour ce cas d'usage). Doit contenir entre 5 et 128 caractères.",
        example = "MonNouveauMotDePasse123!",
        minLength = 5,
        maxLength = 128,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
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
