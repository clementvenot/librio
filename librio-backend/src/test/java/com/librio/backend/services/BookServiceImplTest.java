package com.librio.backend.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import com.librio.backend.entities.Book;
import com.librio.backend.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BookServiceImplTest {

    @Autowired
    private IBookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void create_shouldPersist_whenExternalIdNotExists() {
        Book b = bookService.create("evQiygAACAAJ", "title");
        assertNotNull(b.getId());
        assertTrue(bookService.existsByExternalId("evQiygAACAAJ"));
        assertThat(bookService.findByExternalId("evQiygAACAAJ")).isPresent();
    }
   
    @Test
    void create_shouldThrow_whenExternalIdAlreadyExists() {
        bookRepository.save(new Book("evQiygAACAA", "title"));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,() -> bookService.create("evQiygAACAA", "title1"));

        assertThat(ex.getMessage()).contains("ExternalId déjà utilisé: evQiygAACAA");
    }
}

