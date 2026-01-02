package com.librio.backend.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FavoriteResponse", description = "Réponse après ajout ou récupération d'un favori.")
public class FavoriteResponseDto {

    @Schema(
        description = "Email de l'utilisateur",
        example = "utilisateur@example.com"
    )
    private String userEmail;

    @Schema(
        description = "Identifiant externe du livre",
        example = "OL123M"
    )
    private String bookExternalId;

    @Schema(
        description = "Statut du favori (par exemple : ACTIVE, REMOVED)",
        example = "ACTIVE"
    )
    private FavoriteStatus status;

    public FavoriteResponseDto() {}

    public FavoriteResponseDto(String userEmail, String bookExternalId, FavoriteStatus status) {
        this.userEmail = userEmail;
        this.bookExternalId = bookExternalId;
        this.status = status;
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

    public FavoriteStatus getStatus() { 
    	return status; 
    	}
    public void setStatus(FavoriteStatus status) { 
    	this.status = status; 
    	}
}
