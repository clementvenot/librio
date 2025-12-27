package com.librio.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "favorites",
       uniqueConstraints = {
           @UniqueConstraint(name = "uq_user_book", columnNames = {"user_id", "book_id"})
       },
       indexes = {
           @Index(name = "idx_favorites_user", columnList = "user_id"),
           @Index(name = "idx_favorites_book", columnList = "book_id")
       })
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_favorites_user"))
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_favorites_book"))
    private Book book;

    protected Favorite() {}

    
    public Favorite(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    public Long getId() { return id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    // equals/hashCode par identité (id) OU par (user, book) si non-persisté
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        Favorite other = (Favorite) o;
        if (id != null && other.id != null) {
            return id.equals(other.id);
        }
        return user != null && book != null &&
               other.user != null && other.book != null &&
               user.equals(other.user) && book.equals(other.book);
    }

    // le patern du 31 * et la val de depart 17
    // recommandé par Joshua Bloch
	@Override
	public int hashCode() {
	    if (id != null) {
	        return id.hashCode();
	    } else {
	        if (user != null && book != null) {
	            int result = 17; // val départ arbitraire
	            result = 31 * result + user.hashCode();
	            result = 31 * result + book.hashCode();
	            return result;
	        } else {
	            return 0;
	        }
	    }
	}

    @Override
    public String toString() {
        return "Favorite{id=" + id +
               ", userEmail=" + (user != null ? user.getEmail() : null) +
               ", bookExternalId=" + (book != null ? book.getExternalId() : null) + "}";
    }
}
