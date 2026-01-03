package com.librio.frontend.services;

import com.librio.frontend.dto.book.BigListBookResponseDto;
import com.librio.frontend.dto.book.CreateBookRequestDto;
import com.librio.frontend.dto.book.LittleListBookResponseDto;
import com.librio.frontend.dto.book.ExistsBookResponseDto;
import com.librio.frontend.dto.book.ErrorResponseDto;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.RestClientResponseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BookServiceFront {

    private final RestClient restClient;

    public BookServiceFront(RestClient restClient) {
        this.restClient = restClient;
    }

    // code 200 ExistsBookResponseDto
    public ExistsBookResponseDto checkExists(String externalId) {
        return restClient.get()
                .uri("/books/{externalId}/exists", externalId)
                .retrieve()
                .body(ExistsBookResponseDto.class);
    }
    
    // code 200 LittleListBookResponseDto
    public List<LittleListBookResponseDto> listSummaries() {
        return restClient.get()
                .uri("/books")
                .retrieve()
                .body(new ParameterizedTypeReference<List<LittleListBookResponseDto>>() {});
    }
    
    // GET /books/{externalId}/full  
    public BigListBookResponseDto getFull(String externalId) {
        return restClient.get()
                .uri("/books/{externalId}/full", externalId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (reqSpec, res) -> { // si la reponse est 404 ErrorResponseDto
                    ErrorResponseDto error = null;
                    try { // tente de lire ErrorResponseDto
                        error = ((ResponseSpec) res).body(ErrorResponseDto.class);
                    } catch (Exception ignore) {} // on ignore
                    String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Aucun livre avec externalId: " + externalId;
                    throw new NotFoundException(msg);
                })
                .body(BigListBookResponseDto.class); // reponse 200 BigListBookResponseDto
    }

    // GET /books/{externalId}/summary  
    public LittleListBookResponseDto getSummary(String externalId) {
        return restClient.get()
                .uri("/books/{externalId}/summary", externalId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (reqSpec, res) -> { // si la reponse est 404 ErrorResponseDto
                    ErrorResponseDto error = null;
                    try { // tente de lire ErrorResponseDto
                        error = ((ResponseSpec) res).body(ErrorResponseDto.class);
                    } catch (Exception ignore) {} // on ignore
                    String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Aucun livre avec externalId: " + externalId;
                    throw new NotFoundException(msg);
                })
                .body(LittleListBookResponseDto.class); // reponse 200 BigListBookResponseDto
    }

    // create /books
    public BigListBookResponseDto create(CreateBookRequestDto req) {
        return restClient.post()
                .uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .body(req)
                .retrieve()
                .onStatus(status -> status.value() == 409, (reqSpec, res) -> {  // 409 ExistsBookResponseDto
                    try { // on lit ExistsBookResponseDto 
                        ExistsBookResponseDto exists = ((ResponseSpec) res).body(ExistsBookResponseDto.class);
                        String extId = (exists != null) ? exists.getExternalId() : null;
                        String msg = (extId != null) ? "Cet externalId est déjà utilisé: " + extId : "Cet externalId est déjà utilisé.";
                        throw new DuplicateExternalIdException(msg);
                    } catch (Exception parseConflict) { //on arrive pas à lire ExistsBookResponseDto 
                        ErrorResponseDto err = null;
                        try { //on lis ErrorResponseDto
                            err = ((ResponseSpec) res).body(ErrorResponseDto.class);
                        } catch (Exception ignore) {} //sinon message par default
                        String msg = (err != null && err.getMessage() != null) ? err.getMessage() : "Cet externalId est déjà utilisé.";
                        throw new DuplicateExternalIdException(msg);
                    }
                })
                .onStatus(status -> status.value() == 400, (reqSpec, res) -> { //si la reponse est 400 ErrorResponseDto
                    ErrorResponseDto error = null;
                    try { // recuperation du back du message 
                        error = ((ResponseSpec) res).body(ErrorResponseDto.class);
                    } catch (Exception ignore) {} // on ignore et on met un message generique
                    String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Requête invalide: champs manquants ou incorrects.";
                    throw new IllegalArgumentException(msg);
                })
                .body(BigListBookResponseDto.class); // si la reponse est 201 BigListBookResponseDto 
    }

    // DELETE /books/{externalId} 
    public void deleteByExternalId(String externalId) {
        ResponseEntity<Void> response = restClient.delete()
                .uri("/books/{externalId}", externalId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (reqSpec, res) -> { // reponse = 404
                    ErrorResponseDto error = null;
                    try { // recuperation du back du message 
                        error = ((ResponseSpec) res).body(ErrorResponseDto.class);
                    } catch (Exception ignore) {}  // on ignore et on met un message generique
                    String msg = (error != null && error.getMessage() != null) ? error.getMessage() : "Aucun livre avec externalId: " + externalId;
                    throw new NotFoundException(msg);
                })
                .toBodilessEntity();
        if (response.getStatusCode().value() != 204) { // reponse = 204
            throw new IllegalStateException("Suppression: code inattendu " + response.getStatusCode().value());
        }
    }
    
    
 
    // GET /books/search?title=&author=&publisher=&categories=&minRating=
    public List<BigListBookResponseDto> search(
            String title,
            String author,
            String publisher,
            String categories,
            Long minRating
    ) {
        try {
            BigListBookResponseDto[] arr = restClient
                    .get()
                    .uri(uriBuilder -> {
                        var b = uriBuilder.path("/books/search");
                        if (title != null && !title.isBlank()) {
                            b.queryParam("title", title);
                        }
                        if (author != null && !author.isBlank()) {
                            b.queryParam("author", author);
                        }
                        if (publisher != null && !publisher.isBlank()) {
                            b.queryParam("publisher", publisher);
                        }
                        if (categories != null && !categories.isBlank()) {
                            b.queryParam("categories", categories);
                        }
                        if (minRating != null) {
                            b.queryParam("minRating", minRating);
                        }
                        return b.build();
                    })
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(BigListBookResponseDto[].class)
                    .getBody();

            return (arr == null) ? Collections.emptyList() : Arrays.asList(arr);

        } catch (RestClientResponseException ex) {
            // En cas d'erreur on renvoie une liste vide 
            return Collections.emptyList();
        }
    }

    // Exceptions personnalisées
    public static class DuplicateExternalIdException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public DuplicateExternalIdException(String msg) { super(msg); }
    }

    public static class NotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public NotFoundException(String msg) { super(msg); }
    }
}
