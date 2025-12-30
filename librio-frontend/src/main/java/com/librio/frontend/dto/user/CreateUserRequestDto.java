package com.librio.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "CreateUserRequest",
    description = "Requête de création d'utilisateur. Fournir un email valide et un mot de passe d'au moins 5 caractères.")
public class CreateUserRequestDto {

    @Schema(
        description = "Email de l'utilisateur",
        example = "toto@mail.com",
        maxLength = 255)
    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    @Size(max = 255)
    private String email;

    @Schema(
        description = "Mot de passe de l'utilisateur.",
        example = "Passw0rd!",
        minLength = 5,
        maxLength = 255,
        format = "password")
    @NotBlank(message = "Mot de passe obligatoire")
    @Size(min = 5, max = 255)
    private String password;

    public CreateUserRequestDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
