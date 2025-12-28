package com.librio.backend.dto.favorite;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RemoveFavoriteRequestDto {

    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    @Size(max = 255)
    private String userEmail;

    @NotBlank(message = "ExternalId obligatoire")
    @Size(max = 100)
    private String bookExternalId;

    public RemoveFavoriteRequestDto() {}

    public RemoveFavoriteRequestDto(String userEmail, String bookExternalId) {
        this.userEmail = userEmail;
        this.bookExternalId = bookExternalId;
    }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getBookExternalId() { return bookExternalId; }
    public void setBookExternalId(String bookExternalId) { this.bookExternalId = bookExternalId; }
}
