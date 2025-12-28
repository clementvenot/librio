
package com.librio.backend.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import com.librio.backend.entities.Book;
import com.librio.backend.entities.Favorite;
import com.librio.backend.entities.User;
import com.librio.backend.repositories.BookRepository;
import com.librio.backend.repositories.FavoriteRepository;
import com.librio.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FavoriteServiceImplTest {

    @Autowired
    private IFavoriteService favoriteService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private Long userId;
    private Long bookId;

    @BeforeEach
    void setup() {
        favoriteRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        User u = userRepository.save(new User("toto@mail.com", "pwd"));
        Book b = bookRepository.save(new Book("evQiygAACAAJ", "title"));
        userId = u.getId();
        bookId = b.getId();
    }

    @Test
    void addFavorite_should_Create_when_NotExists() {
        Favorite fav = favoriteService.addFavorite(userId, bookId);
        assertNotNull(fav.getId());
        assertTrue(favoriteService.exists(userId, bookId));
        assertThat(favoriteService.find(userId, bookId)).isPresent();
    }

    @Test
    void addFavorite_should_Throw_if_AlreadyExists() {
        favoriteService.addFavorite(userId, bookId);

        IllegalStateException ex = assertThrows(IllegalStateException.class,() -> favoriteService.addFavorite(userId, bookId));
        assertThat(ex.getMessage()).contains("Favori existe déjà");
    }

    @Test
    void addFavorite_should_Throw_if_UserMissing() {

    	userRepository.deleteById(userId);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,() -> favoriteService.addFavorite(userId, bookId));
        assertThat(ex.getMessage()).contains("User introuvable");
    }

    @Test
    void addFavorite_should_Throw_if_BookMissing() {

    	bookRepository.deleteById(bookId);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,() -> favoriteService.addFavorite(userId, bookId));
        assertThat(ex.getMessage()).contains("Book introuvable");
    }

    @Test
    void removeFavorite_should_Delete_if_Present() {
        favoriteService.addFavorite(userId, bookId);
        assertTrue(favoriteService.exists(userId, bookId));

        favoriteService.removeFavorite(userId, bookId);
        assertFalse(favoriteService.exists(userId, bookId));
        assertThat(favoriteService.find(userId, bookId)).isEmpty();
    }
}
