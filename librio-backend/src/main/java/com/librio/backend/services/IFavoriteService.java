package com.librio.backend.services;

import java.util.List;
import java.util.Optional;
import com.librio.backend.entities.Favorite;

public interface IFavoriteService {
	
	Favorite addFavorite(Long userId, Long bookId);    
	void removeFavorite(Long userId, Long bookId);    
	Optional<Favorite> find(Long userId, Long bookId);    
	boolean exists(Long userId, Long bookId);
    List<Favorite> findAllByUser_Email(String email);}
