package com.librio.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.librio.backend.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);    
	boolean existsByEmail(String email);
	
	@Modifying    
	@Transactional    
	@Query("UPDATE User u SET u.password = :newPassword WHERE u.email = :email")    
	int updatePasswordByEmail(@Param("email") String email, @Param("newPassword") String newPassword);

}
