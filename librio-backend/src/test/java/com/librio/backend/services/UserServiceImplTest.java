package com.librio.backend.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import com.librio.backend.entities.User;
import com.librio.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRepository userRepository;

	
	@Test
	void register_should_CreateUser_when_EmailNotExists() {

		User saved = userService.register("titi@mail.com", "secret");
	
	    assertNotNull(saved.getId());
	    assertThat(saved.getEmail()).isEqualTo("titi@mail.com");
	    assertTrue(userService.existsByEmail("titi@mail.com"));
	    assertThat(userService.findByEmail("titi@mail.com")).isPresent();
	}


    @Test
    void register_should_Throw_when_EmailAlreadyUsed() {

    	userRepository.save(new User("tata@mail.com", "pwd"));

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> userService.register("tata@mail.com", "new")
        );
        assertThat(ex.getMessage()).contains("Email déjà utilisé: tata@mail.com");
    }

    @Test
    void findByEmail_should_Return_Empty_when_NotFound() {
        assertThat(userService.findByEmail("no@mail.com")).isEmpty();
    }
}
