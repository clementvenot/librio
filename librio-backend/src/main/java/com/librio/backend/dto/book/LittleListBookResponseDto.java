package com.librio.backend.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "LittleListBookResponseDto",
    description = "Réponse simplifiée pour afficher une liste compacte de livres."
)
public class LittleListBookResponseDto {

    @NotBlank
    @Size(max = 500)
    @Schema(
        description = "Titre du livre.",
        example = "Clean Code",
        maxLength = 500
    )
    private String title;

    @Size(max = 500)
    @Schema(
        description = "Auteur(s) du livre.",
        example = "Robert C. Martin",
        maxLength = 500,
        nullable = true
    )
    private String author;

    @Size(max = 255)
    @Schema(
        description = "Éditeur.",
        example = "Prentice Hall",
        maxLength = 255,
        nullable = true
    )
    private String publisher;

    @Size(max = 500)
    @Schema(
        description = "Catégories/genres.",
        example = "Software Engineering, Programming",
        maxLength = 500,
        nullable = true
    )
    private String categories;

    @NotBlank
    @Size(max = 100)
    @Schema(
        description = "Identifiant externe (ex: Google Books ID, ISBN-13, etc.).",
        example = "gBoDDAAAQBAJ",
        maxLength = 100
    )
    private String externalId;

    public LittleListBookResponseDto() {}

    public LittleListBookResponseDto(String title, String author, String publisher, String categories, String externalId) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.categories = categories;
        this.externalId = externalId;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}


}
