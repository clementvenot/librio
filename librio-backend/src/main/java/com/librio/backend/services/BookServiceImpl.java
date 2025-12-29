package com.librio.backend.services;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.librio.backend.dto.book.CreateBookRequestDto;
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
    public Book create(CreateBookRequestDto req) {        
    	if (bookRepository.existsByExternalId(req.getExternalId())) {            
    		throw new IllegalArgumentException("ExternalId déjà utilisé: " + req.getExternalId());       
    		}        
    	Book b = new Book(req.getExternalId(), req.getTitle());        
    	b.setSubtitle(req.getSubtitle());        
    	b.setAuthor(req.getAuthor());        
    	b.setPublisher(req.getPublisher());        
    	b.setPublishedDate(req.getPublishedDate());        
    	b.setCategories(req.getCategories());        
    	b.setPageCount(req.getPageCount());        
    	b.setAverageRating(req.getAverageRating());        
    	b.setImageMedium(req.getImageMedium());        
    	
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
