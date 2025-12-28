package com.librio.backend.mappers;

import com.librio.backend.LibrioBackendApplication;
import com.librio.backend.dto.user.CreateUserRequestDto;
import com.librio.backend.dto.user.LoginResponseDto;
import com.librio.backend.dto.user.UserExistResponseDto;
import com.librio.backend.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'intégration léger : injection du bean MapStruct généré via Spring.
 * Nécessite que l'annotation processing MapStruct soit active (OK dans ton POM).
 */
@SpringBootTest(classes = LibrioBackendApplication.class)
class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void toEntity_shouldIgnoreId_andCopyFields() {
        // Arrange
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setEmail("user@example.com");
        dto.setPassword("clearPwd"); // le hashing se fait côté service

        // Act
        User entity = mapper.toEntity(dto);

        // Assert
        assertNull(entity.getId());
        assertEquals("user@example.com", entity.getEmail());
        assertEquals("clearPwd", entity.getPassword());
    }

    @Test
    void toLoginResponse_shouldBuildDto() {
        // Act
        LoginResponseDto ok = mapper.toLoginResponse(true, "Authentification réussie");
        LoginResponseDto ko = mapper.toLoginResponse(false, "Erreur");

        // Assert
        assertTrue(ok.isAuthenticated());
        assertEquals("Authentification réussie", ok.getMessage());

        assertFalse(ko.isAuthenticated());
        assertEquals("Erreur", ko.getMessage());
    }

    @Test
    void toUserExistResponse_shouldBuildDto() {
        // Act
        UserExistResponseDto resp = mapper.toUserExistResponse("user@example.com", true);

        // Assert
        assertEquals("user@example.com", resp.getEmail());
        assertTrue(resp.isExists());
    }

    // --- tests additionnels utiles ---

    @Test
    void updateEntityFromCreateDto_shouldIgnoreNulls_andKeepExistingValues() {
        // Arrange: entité existante en base (simulée)
        User entity = new User("old@example.com", "hashedOldPwd");

        // DTO de mise à jour partielle : on change l'email, on laisse password à null
        CreateUserRequestDto patch = new CreateUserRequestDto();
        patch.setEmail("new@example.com");
        patch.setPassword(null); // doit être ignoré par le mapper

        // Act
        mapper.updateEntityFromCreateDto(patch, entity);

        // Assert
        assertEquals("new@example.com", entity.getEmail());
        assertEquals("hashedOldPwd", entity.getPassword()); // inchangé
    }

    @Test
    void toEntity_shouldNotSetId_andAcceptMinimalFields() {
        // Arrange
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setEmail("minimal@example.com");
        dto.setPassword("pwd");

        // Act
        User entity = mapper.toEntity(dto);

        // Assert
        assertNull(entity.getId());
        assertEquals("minimal@example.com", entity.getEmail());
        assertEquals("pwd", entity.getPassword());
    }
}
