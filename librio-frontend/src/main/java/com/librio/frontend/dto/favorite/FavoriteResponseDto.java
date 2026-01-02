package com.librio.frontend.dto.favorite;


public class FavoriteResponseDto {

    private String userEmail;
    private String bookExternalId;
    private FavoriteStatus status;

    public FavoriteResponseDto() {}

    public FavoriteResponseDto(String userEmail, String bookExternalId, FavoriteStatus status) {
        this.userEmail = userEmail;
        this.bookExternalId = bookExternalId;
        this.status = status;
    }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getBookExternalId() { return bookExternalId; }
    public void setBookExternalId(String bookExternalId) { this.bookExternalId = bookExternalId; }

    public FavoriteStatus getStatus() { return status; }
    public void setStatus(FavoriteStatus status) { this.status = status; }
}
