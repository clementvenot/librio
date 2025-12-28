package com.librio.backend.mappers;

import com.librio.backend.dto.favorite.FavoriteResponseDto;
import com.librio.backend.dto.favorite.FavoriteStatus;
import com.librio.backend.entities.Book;
import com.librio.backend.entities.Favorite;
import com.librio.backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    /* Création Favorite (User + Book -> Favorite) */
    @Mapping(target = "id", ignore = true) // id géré par DB
    @Mapping(target = "user", source = "user")
    @Mapping(target = "book", source = "book")
    Favorite toEntity(User user, Book book);

    /* Entity -> DTO (avec statut passé en paramètre) */
    @Mapping(target = "userEmail",      source = "favorite.user.email")
    @Mapping(target = "bookExternalId", source = "favorite.book.externalId")
    @Mapping(target = "status",         source = "status")
    FavoriteResponseDto toResponse(Favorite favorite, FavoriteStatus status);

    /* Liste d'entities -> Liste de DTOs (default method pour gérer le paramètre status) */
    default List<FavoriteResponseDto> toResponses(List<Favorite> favorites, FavoriteStatus status) {
        if (favorites == null || favorites.isEmpty()) {
            return Collections.emptyList();
        }
        List<FavoriteResponseDto> out = new ArrayList<>(favorites.size());
        for (Favorite f : favorites) {
            out.add(toResponse(f, status));
        }
        return out;
    }

    // Réponses pratiques
    default FavoriteResponseDto toAddedResponse(Favorite favorite) {
        return toResponse(favorite, FavoriteStatus.ADDED);
    }

    default FavoriteResponseDto toRemovedResponse(Favorite favorite) {
        return toResponse(favorite, FavoriteStatus.REMOVED);
    }

    default FavoriteResponseDto toAlreadyExistsResponse(Favorite favorite) {
        return toResponse(favorite, FavoriteStatus.ALREADY_EXISTS);
    }

    default FavoriteResponseDto toNotFoundResponse(String userEmail, String bookExternalId) {
        return new FavoriteResponseDto(userEmail, bookExternalId, FavoriteStatus.NOT_FOUND);
    }
}
