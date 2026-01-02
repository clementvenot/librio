package com.librio.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librio.backend.entities.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    boolean existsByUserIdAndBookId(Long userId, Long bookId);    
    Optional<Favorite> findByUserIdAndBookId(Long userId, Long bookId);
    List<Favorite> findAllByUser_Email(String email);}
