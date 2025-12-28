package com.librio.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "books", indexes = {    
		@Index(name = "ix_books_external_id", columnList = "external_id", unique = true)})
public class Book {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id",unique = true, nullable = false, length = 100)
    private String externalId;

    @NotBlank
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "subtitle", length = 500)
    private String subtitle;

    @Column(name = "author", length = 500)
    private String author;

    @Column(name = "publisher", length = 255)
    private String publisher;

    @Column(name = "published_date", length = 20)
    private String publishedDate;

    @Column(name = "categories", length = 500)
    private String categories;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "average_rating")
    private Long averageRating;

    @Column(name = "image_medium", length = 500)
    private String imageMedium;
    
    // constructor 
    public Book() {}
    
    public Book(String external_id,String title) {
    	this.externalId = external_id;
    	this.title = title;
    }

    // Getters/Setters

    public Long getId() { return id; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getPublishedDate() { return publishedDate; }
    public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }

    public String getCategories() { return categories; }
    public void setCategories(String categories) { this.categories = categories; }

    public Integer getPageCount() { return pageCount; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }

    public Long getAverageRating() { return averageRating; }
    public void setAverageRating(Long averageRating) { this.averageRating = averageRating; }

    public String getImageMedium() { return imageMedium; }
    public void setImageMedium(String imageMedium) { this.imageMedium = imageMedium; }
    
    @Override    
    public boolean equals(Object o) {
    	if (this == o) return true;        
    	if (!(o instanceof Book)) return false;        
    	Book other = (Book) o;        
    	return externalId != null && externalId.equals(other.externalId);    
    	}    
    
    @Override    
    public int hashCode() {        
    	return externalId != null ? externalId.hashCode() : 0;    
    	}    
    
    @Override    
    public String toString() {        
    	return "Book{externalId='" + externalId + "', "
    			+ "title='" + title + "'}";    }
}

