package com.librio.backend.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.librio.backend.entities.Book;
import com.librio.backend.entities.Favorite;
import com.librio.backend.entities.User;
import com.librio.backend.repositories.BookRepository;
import com.librio.backend.repositories.FavoriteRepository;
import com.librio.backend.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional; 
//pour obtenir le read only (permet d'économiser coût cpu memoire)

@Service
@Transactional
class FavoriteServiceImpl implements IFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    FavoriteServiceImpl(FavoriteRepository favoriteRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }


	@Override
	public Favorite addFavorite(Long userId, Long bookId) {

		if (favoriteRepository.existsByUserIdAndBookId(userId, bookId)) {
	        throw new IllegalStateException("Favori existe déjà pour (userId=" + userId + ", bookId=" + bookId + ")");
	    }
	
	    Optional<User> optUser = userRepository.findById(userId);
	    if (optUser.isEmpty()) {
	        throw new IllegalArgumentException("User introuvable: " + userId);
	    }
	
	    Optional<Book> optBook = bookRepository.findById(bookId);
	    if (optBook.isEmpty()) {
	        throw new IllegalArgumentException("Book introuvable: " + bookId);
	    }
	    
	    User user = optUser.get();
	    Book book = optBook.get();
	    Favorite fav = new Favorite(user, book);
	    return favoriteRepository.save(fav);
	}

    @Override
    public void removeFavorite(Long userId, Long bookId) {
        favoriteRepository.findByUserIdAndBookId(userId, bookId)
            .ifPresent(favoriteRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Favorite> find(Long userId, Long bookId) {
        return favoriteRepository.findByUserIdAndBookId(userId, bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long userId, Long bookId) {
        return favoriteRepository.existsByUserIdAndBookId(userId, bookId);
    }
}

