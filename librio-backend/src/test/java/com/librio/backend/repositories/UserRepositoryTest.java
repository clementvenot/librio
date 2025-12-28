package com.librio.backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.librio.backend.entities.User;

@DataJpaTest 
@ActiveProfiles("test")
public class UserRepositoryTest {
	
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @DisplayName("existsByEmail doit retourner true quand l'entité existe")
    void existsByEmail_should_return_true_when_exists() {

    	userRepository.save(new User("toto@mail.com", "pwd"));

        assertThat(userRepository.existsByEmail("toto@mail.com")).isTrue();
        assertThat(userRepository.existsByEmail("tata@mail.com")).isFalse();
    }  
    
    @Test
    @DisplayName("findByEmail doit retourner l'entité quand elle existe")
    void findByEmail_should_return_entity_when_exists() {
    	
    	User u1 = userRepository.save(new User("toto@mail.com","pass"));

        Optional<User> UoptFound = userRepository.findByEmail("toto@mail.com");

        assertThat(UoptFound).isPresent();
        User found = UoptFound.get();
        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(u1.getId());
        assertThat(found.getEmail()).isEqualTo("toto@mail.com");
    }
    
    @Test
    @DisplayName("findByEmail doit retourner vide quand l'entité n'existe pas")
    void findByEmail_should_return_empty_when_not_exists() {

    	Optional<User> UoptFound = userRepository.findByEmail("No_mail");

        assertThat(UoptFound).isNotPresent();
    }
	
}
