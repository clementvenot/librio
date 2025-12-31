package com.librio.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateUserResponse", description = "Réponse après création d'utilisateur.")
public class CreateUserResponseDto {

    @Schema(description = "Identifiant utilisateur", 
    		example = "101",
    		maxLength = 255)
    private Long id;

    @Schema(description = "Email créé", 
    		example = "new@example.com")
    private String email;

    @Schema(description = "Message de confirmation", 
    		example = "Compte créé avec succès")
    private String message;

    public CreateUserResponseDto() {}

    public CreateUserResponseDto(Long id, String email, String message) {
        this.id = id; this.email = email; this.message = message;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
