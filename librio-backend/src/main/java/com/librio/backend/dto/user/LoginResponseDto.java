package com.librio.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse", description = "Réponse de la tentative de connexion.")
public class LoginResponseDto {

    @Schema(description = "État d'authentification", example = "true")
    private boolean authenticated;

    @Schema(description = "Message d'information", example = "Connexion réussie")
    private String message;

    public LoginResponseDto() {}
    public LoginResponseDto(boolean authenticated, String message) {
        this.authenticated = authenticated;
        this.message = message;
    }
    public boolean isAuthenticated() { return authenticated; }
    public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
