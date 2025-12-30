package com.librio.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
	    name = "UserExistRequest",
	    description = "Requête pour vérifier si un utilisateur existe par email.")
	public class UserExistRequestDto {

	    @Schema(description = "Email à vérifier", 
	    		example = "someone@example.com", 
	    		maxLength = 255)
	    @Email(message = "Email invalide")
	    @NotBlank(message = "Email obligatoire")
	    @Size(max = 255)
	    private String email;

    public UserExistRequestDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}



