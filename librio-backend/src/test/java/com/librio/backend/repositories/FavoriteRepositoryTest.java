package com.librio.backend.repositories;

import com.librio.backend.entities.Book;
import com.librio.backend.entities.Favorite;
import com.librio.backend.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("existsByUserIdAndBookId retourne true si le favori existe")
    void existsByUserIdAndBookId_should_return_true_when_exists() {
    	
        User u = new User("toto@mail.com", "pwd");
        userRepository.save(u);
        Book b = new Book("evQiygAACAAJ", "Title");
        bookRepository.save(b);
        Favorite f =  new Favorite(u, b);
        favoriteRepository.save(f);

        assertThat(favoriteRepository.existsByUserIdAndBookId(u.getId(), b.getId())).isTrue();
    }

    @Test
    @DisplayName("existsByUserIdAndBookId retourne false si le favori n'existe pas")
    void existsByUserIdAndBookId_should_return_false_when_not_exists() {
   
        User u = new User("toto@mail.com","pwd");
        userRepository.save(u);
        Book b = new Book("evQiygAACAAJ", "Title");
        bookRepository.save(b);

        assertThat(favoriteRepository.existsByUserIdAndBookId(u.getId(), b.getId())).isFalse();
    }

    @Test
    @DisplayName("findByUserIdAndBookId retourne l'entité quand elle existe")
    void findByUserIdAndBookId_should_return_entity_when_exists() {
        
        User u = new User("toto@mail.com","pwd");
        userRepository.save(u);
        Book b = new Book("evQiygAACAAJ", "Title");
        bookRepository.save(b);
        Favorite f =  new Favorite(u, b);
        favoriteRepository.save(f);

        Optional<Favorite> FoptFound = favoriteRepository.findByUserIdAndBookId(u.getId(), b.getId());

        assertThat(FoptFound).isPresent();
        Favorite found = FoptFound.get();
        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(f.getId());
        assertThat(found.getUser().getEmail()).isEqualTo("toto@mail.com");
        assertThat(found.getBook().getExternalId()).isEqualTo("evQiygAACAAJ");
    }

    @Test
    @DisplayName("findByUserIdAndBookId retourne vide quand le favori n'existe pas")
    void findByUserIdAndBookId_should_return_empty_when_not_exists() {

    	User u = new User("toto@mail.com","pwd");
        userRepository.save(u);
        Book b = new Book("evQiygAACAAJ", "Title");
        bookRepository.save(b);

        Optional<Favorite> FoptFound = favoriteRepository.findByUserIdAndBookId(u.getId(), b.getId());

        assertThat(FoptFound).isNotPresent();
    }

}
