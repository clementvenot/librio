package com.librio.backend.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@Table(name = "users", indexes = {    
		@Index(name = "ix_users_email", columnList = "email", unique = true)})
public class User {
	
	@Id
	@Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
	@Column(name = "email", nullable = false, unique = true, length = 255 )
	private String email;
	
    @NotBlank(message = "Mot de passe obligatoire")
	@Column(name = "password", nullable = false, length = 255)
	private String password;
	
	// constructor
	
	protected User() {}
	
	// getters / setters
	
	public Long getId() { return id;}

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	@Override
	public int hashCode() {
        return email != null ? email.hashCode() : 0;
	}

	@Override
	public boolean equals(Object o) {
        if (this == o) return true;        
        if (!(o instanceof User)) 
        	return false;        
        User other = (User) o;        
        return email != null && email.equals(other.email);
	}

	@Override
	public String toString() {
        return "User{id=" + id + ", email='" + email + "'}";
	}
	

}
