package com.librio.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "UpdatePasswordResponseDto",
    description = "Réponse renvoyée après la mise à jour du mot de passe d'un utilisateur."
)
public class UpdatePasswordResponseDto {

    @Schema(
        description = "Email de l'utilisateur concerné par la mise à jour",
        example = "user@example.com"
    )
    private String email;

    @Schema(
        description = "Message indiquant le résultat de l'opération",
        example = "Mot de passe mis à jour"
    )
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
