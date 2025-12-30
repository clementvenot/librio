package com.librio.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "LoginRequest",
		description = "Requête de connexion: email + mot de passe.")
public class LoginRequestDto {  
	
	@Schema(description = "Email de connexion", 
			example = "new.user@example.com", 
			maxLength = 255)    
	@Email(message = "Email invalide")    
	@NotBlank(message = "Email obligatoire")    
	@Size(max = 255)    
	private String email;   
	
	@Schema(description = "Mot de passe (write-only)", 
			example = "Passw0rd!", 
			minLength = 4, 
			maxLength = 255,            
			format = "password" )    
	@NotBlank(message = "Mot de passe obligatoire")    
	@Size(min = 4, 
		  max = 255)    
	private String password;
	
    public LoginRequestDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
