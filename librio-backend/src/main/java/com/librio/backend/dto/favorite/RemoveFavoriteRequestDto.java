package com.librio.backend.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "RemoveFavoriteRequest", description = "Requête pour supprimer un livre des favoris d'un utilisateur.")
public class RemoveFavoriteRequestDto {

    @Schema(
        description = "Email de l'utilisateur",
        example = "utilisateur@example.com",
        maxLength = 255,
        format = "email",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    @Size(max = 255)
    private String userEmail;

    @Schema(
        description = "Identifiant externe du livre à retirer des favoris",
        example = "OL123M",
        maxLength = 100,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "ExternalId obligatoire")
    @Size(max = 100)
    private String bookExternalId;

    public RemoveFavoriteRequestDto() {}

    public RemoveFavoriteRequestDto(String userEmail, String bookExternalId) {
        this.userEmail = userEmail;
        this.bookExternalId = bookExternalId;
    }

    public String getUserEmail() { 
    	return userEmail; 
    	}
    public void setUserEmail(String userEmail) { 
    	this.userEmail = userEmail; 
    	}

    public String getBookExternalId() { 
    	return bookExternalId; 
    	}
    public void setBookExternalId(String bookExternalId) { 
    	this.bookExternalId = bookExternalId; 
    	}
}
