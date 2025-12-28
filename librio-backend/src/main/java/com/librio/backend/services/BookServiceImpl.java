package com.librio.backend.services;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.librio.backend.entities.Book;
import com.librio.backend.repositories.BookRepository;
import org.springframework.transaction.annotation.Transactional; 
//pour obtenir le read only (permet d'économiser coût cpu memoire)

@Service
@Transactional 
class BookServiceImpl implements IBookService {

    private final BookRepository bookRepository;

    BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book create(String externalId, String title) {
        if (bookRepository.existsByExternalId(externalId)) {
            throw new IllegalArgumentException("ExternalId déjà utilisé: " + externalId);
        }
        Book b = new Book(externalId,title);
        return bookRepository.save(b);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findByExternalId(String externalId) {
        return bookRepository.findByExternalId(externalId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByExternalId(String externalId) {
        return bookRepository.existsByExternalId(externalId);
    }
}
