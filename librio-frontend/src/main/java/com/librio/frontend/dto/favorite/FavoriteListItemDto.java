package com.librio.frontend.dto.favorite;


public class FavoriteListItemDto {
    private String userEmail;

    private String bookExternalId;

    public FavoriteListItemDto() {}

    public FavoriteListItemDto(String userEmail, String bookExternalId) {
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

