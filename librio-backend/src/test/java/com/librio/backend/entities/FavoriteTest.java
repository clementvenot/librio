package com.librio.backend.entities;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FavoriteTest {
	
	private Book b1 = new Book();
	private Book b2 = new Book();
	
	private User u1 = new User();
	private User u2 = new User();
	
	@Test
	void equals_should_compare_users_and_books() {
		
		b1.setExternalId("evQiygAACAAJ");
		b2.setExternalId("evQiygAACAAJ");
		
		u1.setEmail("toto@mail.com");
		u2.setEmail("toto@mail.com");

	    Favorite f1 = new Favorite(u1,b1);
	    Favorite f2 = new Favorite(u2,b2);
	
	    assertThat(f1).isEqualTo(f2);
	}
	
	@Test 
	void hashcode_should_be_same_for_same_externalId() {
		b1.setExternalId("evQiygAACAAJ");
		b2.setExternalId("evQiygAACAAJ");
		
		u1.setEmail("toto@mail.com");
		u2.setEmail("toto@mail.com");

	    Favorite f1 = new Favorite(u1,b1);
	    Favorite f2 = new Favorite(u2,b2);
	    
	    assertThat(f1.hashCode()).isEqualTo(f2.hashCode());
	}

}
