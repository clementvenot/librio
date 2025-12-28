package com.librio.backend.services;

import java.util.Optional;
import com.librio.backend.entities.User;

public interface IUserService {
	
	User register(String email, String password);    
	Optional<User> findByEmail(String email);    
	boolean existsByEmail(String email);

}
