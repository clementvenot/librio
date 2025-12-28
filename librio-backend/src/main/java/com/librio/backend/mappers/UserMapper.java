package com.librio.backend.mappers;

import com.librio.backend.dto.user.CreateUserRequestDto;
import com.librio.backend.dto.user.LoginResponseDto;
import com.librio.backend.dto.user.UserExistResponseDto;
import com.librio.backend.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /* Création Entity (DTO -> Entity) */
    @Mapping(target = "id", ignore = true) 
    User toEntity(CreateUserRequestDto dto);

    /* Update */
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromCreateDto(CreateUserRequestDto dto, @MappingTarget User entity);

    // response login.
    default LoginResponseDto toLoginResponse(boolean authenticated, String message) {
        return new LoginResponseDto(authenticated, message);
    }

    // réponse d'existence d'utilisateur.
    default UserExistResponseDto toUserExistResponse(String email, boolean exists) {
        return new UserExistResponseDto(email, exists);
    }
}
