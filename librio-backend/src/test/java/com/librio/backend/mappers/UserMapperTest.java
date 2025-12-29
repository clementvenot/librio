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

@SpringBootTest(classes = LibrioBackendApplication.class)
class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void toEntity_shouldIgnoreId_andCopyFields() {

    	CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setEmail("user@example.com");
        dto.setPassword("clearPwd"); 

        User entity = mapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals("user@example.com", entity.getEmail());
        assertEquals("clearPwd", entity.getPassword());
    }

    @Test
    void toLoginResponse_shouldBuildDto() {

    	LoginResponseDto ok = mapper.toLoginResponse(true, "Authentification réussie");
        LoginResponseDto ko = mapper.toLoginResponse(false, "Erreur");

        assertTrue(ok.isAuthenticated());
        assertEquals("Authentification réussie", ok.getMessage());

        assertFalse(ko.isAuthenticated());
        assertEquals("Erreur", ko.getMessage());
    }

    @Test
    void toUserExistResponse_shouldBuildDto() {

    	UserExistResponseDto resp = mapper.toUserExistResponse("user@example.com", true);

        assertEquals("user@example.com", resp.getEmail());
        assertTrue(resp.isExists());
    }

    @Test
    void updateEntityFromCreateDto_shouldIgnoreNulls_andKeepExistingValues() {

    	User entity = new User("old@example.com", "hashedOldPwd");

        CreateUserRequestDto patch = new CreateUserRequestDto();
        patch.setEmail("new@example.com");
        patch.setPassword(null); 

        mapper.updateEntityFromCreateDto(patch, entity);

        assertEquals("new@example.com", entity.getEmail());
        assertEquals("hashedOldPwd", entity.getPassword()); 
    }

    @Test
    void toEntity_shouldNotSetId_andAcceptMinimalFields() {

    	CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setEmail("minimal@example.com");
        dto.setPassword("pwd");

        User entity = mapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals("minimal@example.com", entity.getEmail());
        assertEquals("pwd", entity.getPassword());
    }
}
