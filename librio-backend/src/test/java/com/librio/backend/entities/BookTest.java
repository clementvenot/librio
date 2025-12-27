package com.librio.backend.entities;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BookTest {

	@Test
	void equals_should_compare_externalId() {
	    Book b1 = new Book();
	    b1.setExternalId("evQiygAACAAJ");
	    Book b2 = new Book();
	    b2.setExternalId("evQiygAACAAJ");
	
	    assertThat(b1).isEqualTo(b2);
	}
	
	@Test 
	void hashcode_should_be_same_for_same_externalId() {
	    Book b1 = new Book();
	    b1.setExternalId("evQiygAACAAJ");
	    Book b2 = new Book();
	    b2.setExternalId("evQiygAACAAJ");
	
	    assertThat(b1.hashCode()).isEqualTo(b2.hashCode());
	}
	
}


