package com.librio.frontend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.librio.frontend.dto.favorite.AddFavoriteRequestDto;
import com.librio.frontend.dto.favorite.FavoriteListItemDto;
import com.librio.frontend.dto.favorite.FavoriteResponseDto;
import com.librio.frontend.dto.favorite.FavoriteStatus;
import com.librio.frontend.dto.favorite.RemoveFavoriteRequestDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FavoriteServiceFront {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FavoriteServiceFront(RestClient restClient) {
        this.restClient = restClient;
    }

    /** GET /favorites?userEmail=...  */
    public List<FavoriteListItemDto> listFavoritesByUserEmail(String userEmail) {
        try {
            FavoriteListItemDto[] arr = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/favorites")
                            .queryParam("userEmail", userEmail)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(FavoriteListItemDto[].class)
                    .getBody();

            List<FavoriteListItemDto> result = (arr == null) ? Collections.emptyList() : Arrays.asList(arr);
            return result;

        } catch (RestClientResponseException ex) {
            // 404 ou autre erreur : on retourne une liste vide
            return Collections.emptyList();
        }
    }

    /** POST /favorites */
    public FavoriteResponseDto addFavorite(AddFavoriteRequestDto req) {
        try {
            FavoriteResponseDto body = restClient
                    .post()
                    .uri("/favorites")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .toEntity(FavoriteResponseDto.class)
                    .getBody();

            return body;
        } catch (RestClientResponseException ex) {
            try {
                // Désérialise en String 
                String json = ex.getResponseBodyAsString();
                if (json != null && !json.isBlank()) {
                    return objectMapper.readValue(json, FavoriteResponseDto.class);
                }
            } catch (Exception ignore) {}
            // ingnore si aucune info 
            return new FavoriteResponseDto(req.getUserEmail(), req.getBookExternalId(), FavoriteStatus.NOT_FOUND);
        }
    }

    /** DELETE /favorites */
    public FavoriteResponseDto removeFavorite(RemoveFavoriteRequestDto req) {
        try {
            FavoriteResponseDto body = restClient
                    .method(HttpMethod.DELETE)
                    .uri("/favorites")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .toEntity(FavoriteResponseDto.class)
                    .getBody();
            return body;
        } catch (RestClientResponseException ex) {
            try {
                // Désérialise  String 
                String json = ex.getResponseBodyAsString();
                if (json != null && !json.isBlank()) {
                    return objectMapper.readValue(json, FavoriteResponseDto.class);
                }
            } catch (Exception ignore) {}
            // ignore si aucune info 
            return new FavoriteResponseDto(req.getUserEmail(), req.getBookExternalId(), FavoriteStatus.NOT_FOUND);
        }
    }

    /** Vérifie l'existence (email + externalId) */
    public boolean exists(String userEmail, String bookExternalId) {
        return listFavoritesByUserEmail(userEmail).stream()
                .anyMatch(it ->
                        it.getUserEmail() != null
                        && it.getBookExternalId() != null
                        && it.getUserEmail().equalsIgnoreCase(userEmail)
                        && it.getBookExternalId().equalsIgnoreCase(bookExternalId)
                );
    }
}

