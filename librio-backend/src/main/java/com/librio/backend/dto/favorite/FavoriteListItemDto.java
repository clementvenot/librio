package com.librio.backend.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FavoriteListItem", description = "Item de la liste des favoris d'un utilisateur.")
public class FavoriteListItemDto {
    @Schema(description = "Email de l'utilisateur", example = "utilisateur@example.com")
    private String userEmail;

    @Schema(description = "Identifiant externe du livre", example = "gbooks:abc123")
    private String bookExternalId;

    public FavoriteListItemDto() {}

    public FavoriteListItemDto(String userEmail, String bookExternalId) {
        this.userEmail = userEmail;
        this.bookExternalId = bookExternalId;
    }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getBookExternalId() { return bookExternalId; }
    public void setBookExternalId(String bookExternalId) { this.bookExternalId = bookExternalId; }
}

