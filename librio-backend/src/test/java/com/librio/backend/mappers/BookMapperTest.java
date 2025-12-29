package com.librio.backend.mappers;

import com.librio.backend.dto.book.BigListBookResponseDto;
import com.librio.backend.dto.book.CreateBookRequestDto;
import com.librio.backend.entities.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookMapperTest {

    @Autowired
    private BookMapper mapper;

    @Test
    void toEntity_shouldIgnoreId_andCopyFields() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setExternalId("EXT-1");
        dto.setTitle("Title");
        dto.setSubtitle("Sub");
        dto.setAuthor("Author");
        dto.setPublisher("Pub");
        dto.setPublishedDate("2024-01-01");
        dto.setCategories("Fiction, Drama");
        dto.setPageCount(123);
        dto.setAverageRating(4L);
        dto.setImageMedium("http://img");

        Book entity = mapper.toEntity(dto);

        assertNull(entity.getId(), "L'id doit être ignoré (géré par la DB)");
        assertEquals("EXT-1", entity.getExternalId());
        assertEquals("Title", entity.getTitle());
        assertEquals("Sub", entity.getSubtitle());
        assertEquals("Author", entity.getAuthor());
        assertEquals("Pub", entity.getPublisher());
        assertEquals("2024-01-01", entity.getPublishedDate());
        assertEquals("Fiction, Drama", entity.getCategories());
        assertEquals(123, entity.getPageCount());
        assertEquals(4L, entity.getAverageRating());
        assertEquals("http://img", entity.getImageMedium());
    }

    @Test
    void toBigListDto_shouldMapAllMatchingFields() {
        Book entity = new Book("EXT-2", "T");
        entity.setSubtitle("S");
        entity.setAuthor("A");
        entity.setPublisher("P");
        entity.setPublishedDate("2023");
        entity.setCategories("Cat");
        entity.setPageCount(10);
        entity.setAverageRating(5L);
        entity.setImageMedium("img");

        BigListBookResponseDto dto = mapper.toBigListDto(entity);

        assertEquals("EXT-2", dto.getExternalId());
        assertEquals("T", dto.getTitle());
        assertEquals("S", dto.getSubtitle());
        assertEquals("A", dto.getAuthor());
        assertEquals("P", dto.getPublisher());
        assertEquals("2023", dto.getPublishedDate());
        assertEquals("Cat", dto.getCategories());
        assertEquals(10, dto.getPageCount());
        assertEquals(5L, dto.getAverageRating());
        assertEquals("img", dto.getImageMedium());
    }

    @Test
    void updateEntityFromCreateDto_shouldIgnoreNulls() {
        Book entity = new Book("EXT-3", "Old title");
        entity.setAuthor("Old author");

        CreateBookRequestDto patch = new CreateBookRequestDto();
        patch.setTitle("New title");
        patch.setAuthor(null);

        mapper.updateEntityFromCreateDto(patch, entity);

        assertEquals("EXT-3", entity.getExternalId());
        assertEquals("New title", entity.getTitle());
        assertEquals(
                "Old author",
                entity.getAuthor(),
                "Le champ null dans le DTO ne doit pas écraser la valeur existante"
        );
    }
}
