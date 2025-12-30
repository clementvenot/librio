package com.librio.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserExistResponse", 
		description = "Résultat de la vérification d'existence de l'email.")
public class UserExistResponseDto {

    @Schema(description = "Email vérifié", 
    		example = "user@example.com")
    private String email;

    @Schema(description = "Indique si l'email existe déjà", 
    		example = "true")
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
