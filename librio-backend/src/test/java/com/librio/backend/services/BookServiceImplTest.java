package com.librio.backend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.librio.backend.dto.book.CreateBookRequestDto;
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

    	CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setExternalId("evQiygAA");
        dto.setTitle("title");
        dto.setSubtitle("subtitle");
        dto.setAuthor("author");
        dto.setPublisher("publisher");
        dto.setPublishedDate("2023-11-10"); 
        dto.setCategories("Fiction, Aventure");
        dto.setPageCount(321);
        dto.setAverageRating(4L);
        dto.setImageMedium("https://example.com/img.jpg");

        Book b = bookService.create(dto);

        assertNotNull(b.getId());
        assertEquals("evQiygAA", b.getExternalId());
        assertEquals("title", b.getTitle());
        assertEquals("subtitle", b.getSubtitle());
        assertEquals("author", b.getAuthor());
        assertEquals("publisher", b.getPublisher());
        assertEquals("2023-11-10", b.getPublishedDate());
        assertEquals("Fiction, Aventure", b.getCategories());
        assertEquals(321, b.getPageCount());
        assertEquals(4L, b.getAverageRating());
        assertEquals("https://example.com/img.jpg", b.getImageMedium());

        assertTrue(bookService.existsByExternalId("evQiygAA"));
        assertThat(bookService.findByExternalId("evQiygAA")).isPresent();
    }

    @Test
    void create_shouldThrow_whenExternalIdAlreadyExists() {
        bookRepository.save(new Book("evQiygAACAA", "title"));

        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setExternalId("evQiygAACAA");
        dto.setTitle("title1");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookService.create(dto));

        assertThat(ex.getMessage()).contains("ExternalId déjà utilisé: evQiygAACAA");
    }
}
