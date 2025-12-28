package com.librio.backend.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LittleListBookResponseDto {

    @NotBlank
    @Size(max = 500)
    private String title;

    @Size(max = 500)
    private String author;

    @Size(max = 255)
    private String publisher;

    @Size(max = 500)
    private String categories;

    @NotBlank
    @Size(max = 100)
    private String externalId;

    public LittleListBookResponseDto() {}

    public LittleListBookResponseDto(String title, String author, String publisher, String categories, String externalId) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.categories = categories;
        this.externalId = externalId;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getCategories() { return categories; }
    public void setCategories(String categories) { this.categories = categories; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
}
