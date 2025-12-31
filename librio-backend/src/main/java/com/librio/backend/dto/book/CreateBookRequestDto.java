
package com.librio.backend.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "CreateBookRequestDto",
    description = "Donnée pour créer un nouveau livre dans la base."
)
public class CreateBookRequestDto {

    @NotBlank
    @Size(max = 100)
    @Schema(
        description = "Identifiant externe (ex: Google Books).",
        example = "gBoDDAAAQBAJ",
        maxLength = 100
    )
    private String externalId;

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
        description = "Sous-titre du livre.",
        example = "A Handbook of Agile Software Craftsmanship",
        maxLength = 500,
        nullable = true
    )
    private String subtitle;

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

    @Size(max = 20)
    @Schema(
        description = "Date de publication (format libre, ex: '2008-08-11').",
        example = "2008-08-11",
        maxLength = 20,
        nullable = true
    )
    private String publishedDate;

    @Size(max = 500)
    @Schema(
        description = "Catégories/genres (ex: séparées par des virgules).",
        example = "Software Engineering, Programming",
        maxLength = 500,
        nullable = true
    )
    private String categories;

    @Schema(
        description = "Nombre de pages.",
        example = "464",
        minimum = "1",
        nullable = true
    )
    private Integer pageCount;

    @Schema(
        description = "Note moyenne (entier, ex: 0–5 ou 0–100 selon votre logique).",
        example = "5",
        nullable = true
    )
    private Long averageRating;

    @Size(max = 500)
    @Schema(
        description = "URL de l’image (taille moyenne).",
        example = "https://example.com/images/clean-code-medium.jpg",
        maxLength = 500,
        nullable = true
    )
    private String imageMedium;

    public CreateBookRequestDto() {}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
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

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Long getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Long averageRating) {
		this.averageRating = averageRating;
	}

	public String getImageMedium() {
		return imageMedium;
	}

	public void setImageMedium(String imageMedium) {
		this.imageMedium = imageMedium;
	}
}
