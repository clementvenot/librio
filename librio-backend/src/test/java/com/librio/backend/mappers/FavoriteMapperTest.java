package com.librio.backend.mappers;

import com.librio.backend.LibrioBackendApplication;
import com.librio.backend.dto.favorite.FavoriteResponseDto;
import com.librio.backend.dto.favorite.FavoriteStatus;
import com.librio.backend.entities.Book;
import com.librio.backend.entities.Favorite;
import com.librio.backend.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LibrioBackendApplication.class)
class FavoriteMapperTest {

    @Autowired
    private FavoriteMapper mapper;

    @Test
    void toEntity_shouldSetUserAndBook_andIgnoreId() {
        // Arrange
        User user = new User("user@example.com", "pwd");
        Book book = new Book("EXT-9", "Title");

        // Act
        Favorite favorite = mapper.toEntity(user, book);

        // Assert
        assertNull(favorite.getId());
        assertNotNull(favorite.getUser());
        assertNotNull(favorite.getBook());
        assertEquals("user@example.com", favorite.getUser().getEmail());
        assertEquals("EXT-9", favorite.getBook().getExternalId());
    }

    @Test
    void toResponse_shouldMapNestedFields_andStatus() {
        // Arrange
        User user = new User("user@example.com", "pwd");
        Book book = new Book("EXT-10", "Title");
        Favorite favorite = mapper.toEntity(user, book);

        // Act
        FavoriteResponseDto dto = mapper.toResponse(favorite, FavoriteStatus.ADDED);

        // Assert
        assertEquals("user@example.com", dto.getUserEmail());
        assertEquals("EXT-10", dto.getBookExternalId());
        assertEquals(FavoriteStatus.ADDED, dto.getStatus());
    }

    @Test
    void toResponses_defaultMethod_shouldReturnList_andPropagateStatus() {
        // Arrange
        User user = new User("u@ex.com", "pwd");
        Book b1 = new Book("B-1", "T1");
        Book b2 = new Book("B-2", "T2");
        Favorite f1 = mapper.toEntity(user, b1);
        Favorite f2 = mapper.toEntity(user, b2);

        // Act
        List<FavoriteResponseDto> dtos = mapper.toResponses(List.of(f1, f2), FavoriteStatus.REMOVED);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("B-1", dtos.get(0).getBookExternalId());
        assertEquals("B-2", dtos.get(1).getBookExternalId());
        assertEquals(FavoriteStatus.REMOVED, dtos.get(0).getStatus());
        assertEquals(FavoriteStatus.REMOVED, dtos.get(1).getStatus());
    }

    @Test
    void convenienceMethods_shouldBuildExpectedStatuses() {
        // Arrange
        User user = new User("u@ex.com", "pwd");
        Book book = new Book("B-3", "T3");
        Favorite fav = mapper.toEntity(user, book);

        // Act & Assert
        FavoriteResponseDto added = mapper.toAddedResponse(fav);
        assertEquals(FavoriteStatus.ADDED, added.getStatus());
        assertEquals("u@ex.com", added.getUserEmail());
        assertEquals("B-3", added.getBookExternalId());

        FavoriteResponseDto removed = mapper.toRemovedResponse(fav);
        assertEquals(FavoriteStatus.REMOVED, removed.getStatus());

        FavoriteResponseDto already = mapper.toAlreadyExistsResponse(fav);
        assertEquals(FavoriteStatus.ALREADY_EXISTS, already.getStatus());
    }

    @Test
    void toNotFoundResponse_shouldReturnStatusNotFound_andEchoInputs() {
        // Act
        FavoriteResponseDto dto = mapper.toNotFoundResponse("ghost@ex.com", "MISS-1");

        // Assert
        assertEquals("ghost@ex.com", dto.getUserEmail());
        assertEquals("MISS-1", dto.getBookExternalId());
        assertEquals(FavoriteStatus.NOT_FOUND, dto.getStatus());
    }
}
