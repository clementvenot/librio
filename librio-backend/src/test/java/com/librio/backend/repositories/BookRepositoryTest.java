package com.librio.backend.repositories;

import com.librio.backend.entities.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest 
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    
    @Test
    @DisplayName("existsByExternalId doit retourner true quand l'entité existe")
    void existsByExternalId_should_return_true_when_exists() {

    	bookRepository.save(new Book("evQiygAACAAJ", "Title1"));

        assertThat(bookRepository.existsByExternalId("evQiygAACAAJ")).isTrue();
        assertThat(bookRepository.existsByExternalId("evQiyg")).isFalse();
    }  
    
    @Test
    @DisplayName("findByExternalId doit retourner l'entité quand elle existe")
    void findByExternalId_should_return_entity_when_exists() {

    	Book b1 = bookRepository.save(new Book("evQiygAACAAJ", "Title1"));

        Optional<Book> BoptFound = bookRepository.findByExternalId("evQiygAACAAJ");

        assertThat(BoptFound).isPresent();
        Book found = BoptFound.get();
        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(b1.getId());
        assertThat(found.getExternalId()).isEqualTo("evQiygAACAAJ");
        assertThat(found.getTitle()).isEqualTo("Title1");
    }

    @Test
    @DisplayName("findByExternalId doit retourner vide quand l'entité n'existe pas")
    void findByExternalId_should_return_empty_when_not_exists() {

    	Optional<Book> b2opt = bookRepository.findByExternalId("No_id");

        assertThat(b2opt).isNotPresent();
    }
    
}
