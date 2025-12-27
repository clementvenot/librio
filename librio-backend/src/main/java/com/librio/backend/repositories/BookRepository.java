package com.librio.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librio.backend.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	
	 Optional<Book> findByExternalId(String externalId);    
	 boolean existsByExternalId(String externalId);

}
