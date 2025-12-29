package com.librio.backend.services;

import java.util.Optional;

import com.librio.backend.dto.book.CreateBookRequestDto;
import com.librio.backend.entities.Book;

public interface IBookService {
	
	Book create(CreateBookRequestDto req);    
	Optional<Book> findByExternalId(String externalId);    
	boolean existsByExternalId(String externalId);

}
