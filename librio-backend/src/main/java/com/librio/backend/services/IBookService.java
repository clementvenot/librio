package com.librio.backend.services;

import java.util.Optional;
import com.librio.backend.entities.Book;

public interface IBookService {
	
	Book create(String externalId, String title);    
	Optional<Book> findByExternalId(String externalId);    
	boolean existsByExternalId(String externalId);

}
