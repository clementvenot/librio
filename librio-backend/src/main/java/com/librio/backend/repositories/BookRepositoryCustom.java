package com.librio.backend.repositories;

import java.util.List;

import com.librio.backend.entities.Book;

public interface BookRepositoryCustom {
    List<Book> searchBooks(String title, String author, String publisher, String categories, Long minRating);
}
