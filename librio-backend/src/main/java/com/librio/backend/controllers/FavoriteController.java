package com.librio.backend.controllers;

import com.librio.backend.dto.favorite.AddFavoriteRequestDto;
import com.librio.backend.dto.favorite.FavoriteListItemDto;
import com.librio.backend.dto.favorite.FavoriteResponseDto;
import com.librio.backend.dto.favorite.FavoriteStatus;
import com.librio.backend.dto.favorite.RemoveFavoriteRequestDto;
import com.librio.backend.entities.Book;
import com.librio.backend.entities.Favorite;
import com.librio.backend.entities.User;
import com.librio.backend.mappers.FavoriteMapper;
import com.librio.backend.repositories.BookRepository;
import com.librio.backend.repositories.UserRepository;
import com.librio.backend.services.IFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Favorites", description = "Donnée pour ajouter/supprimer/afficher list des favoris du couple user-email / book-externalId")
public class FavoriteController {

    private final IFavoriteService favoriteService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final FavoriteMapper favoriteMapper;

    public FavoriteController(IFavoriteService favoriteService,
                              UserRepository userRepository,
                              BookRepository bookRepository,
                              FavoriteMapper favoriteMapper) {
        this.favoriteService = favoriteService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.favoriteMapper = favoriteMapper;
    }

    // ---------------------------------------------------------------------
    // POST /api/favorites  -  Ajouter un favori (email + externalId)
    // ---------------------------------------------------------------------
    @Operation(
        summary = "Ajouter un favori",
        description = "Ajoute un favori à partir de l'email utilisateur et de l'externalId du livre. " +
                      "Renvoie ADDED si ajouté, ALREADY_EXISTS si le favori existait déjà."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Favori créé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FavoriteResponseDto.class),
                examples = @ExampleObject(
                    name = "Ajout réussi",
                    value = "{\"userEmail\":\"user@example.com\",\"bookExternalId\":\"gbooks:abc123\",\"status\":\"ADDED\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "201",
            description = "Déjà existant",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FavoriteResponseDto.class),
                examples = @ExampleObject(
                    name = "Déjà en favoris",
                    value = "{\"userEmail\":\"user@example.com\",\"bookExternalId\":\"gbooks:abc123\",\"status\":\"ALREADY_EXISTS\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Utilisateur ou livre introuvable"),
        @ApiResponse(responseCode = "400", description = "Requête invalide (validation)")
    })
    @PostMapping("/favorites")
    public ResponseEntity<?> addFavorite(@Valid @RequestBody AddFavoriteRequestDto dto) {

        // verif user et book
        Optional<User> optUser = userRepository.findByEmail(dto.getUserEmail());
        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FavoriteResponseDto(dto.getUserEmail(), dto.getBookExternalId(), FavoriteStatus.NOT_FOUND));
        }

        Optional<Book> optBook = bookRepository.findByExternalId(dto.getBookExternalId());
        if (optBook.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FavoriteResponseDto(dto.getUserEmail(), dto.getBookExternalId(), FavoriteStatus.NOT_FOUND));
        }

        User user = optUser.get();
        Book book = optBook.get();

        // Exist ?
        if (favoriteService.exists(user.getId(), book.getId())) {
            Favorite pseudo = new Favorite(user, book); // pour mapper un DTO cohérent
            FavoriteResponseDto body = favoriteMapper.toAlreadyExistsResponse(pseudo);
            return ResponseEntity.ok(body); // 201 ALREADY_EXISTS
        }

        // Add
        Favorite created = favoriteService.addFavorite(user.getId(), book.getId());
        FavoriteResponseDto body = favoriteMapper.toAddedResponse(created);
        URI location = URI.create(String.format("/api/favorites/%s/%s", dto.getUserEmail(), dto.getBookExternalId()));

        return ResponseEntity.created(location).body(body); // 200 ADDED
    }

    // ---------------------------------------------------------------------
    // DELETE /api/favorites  -  Retirer un favori (email + externalId)
    // ---------------------------------------------------------------------
    @Operation(
        summary = "Retirer un favori",
        description = "Supprime le favori identifié par (email utilisateur, externalId du livre). " +
                      "Renvoie REMOVED si supprimé, NOT_FOUND si non présent."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Favori supprimé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FavoriteResponseDto.class),
                examples = @ExampleObject(
                    name = "Supprimé",
                    value = "{\"userEmail\":\"user@example.com\",\"bookExternalId\":\"gbooks:abc123\",\"status\":\"REMOVED\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Favori, utilisateur ou livre introuvable",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FavoriteResponseDto.class),
                examples = @ExampleObject(
                    name = "Non trouvé",
                    value = "{\"userEmail\":\"user@example.com\",\"bookExternalId\":\"gbooks:abc123\",\"status\":\"NOT_FOUND\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Requête invalide (validation)")
    })
    @DeleteMapping("/favorites")
    public ResponseEntity<?> removeFavorite(@Valid @RequestBody RemoveFavoriteRequestDto dto) {

        // verif user book
        Optional<User> optUser = userRepository.findByEmail(dto.getUserEmail());
        Optional<Book> optBook = bookRepository.findByExternalId(dto.getBookExternalId());

        if (optUser.isEmpty() || optBook.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(favoriteMapper.toNotFoundResponse(dto.getUserEmail(), dto.getBookExternalId()));
        }

        User user = optUser.get();
        Book book = optBook.get();

        // recup fav
        Optional<Favorite> optFav = favoriteService.find(user.getId(), book.getId());
        if (optFav.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(favoriteMapper.toNotFoundResponse(dto.getUserEmail(), dto.getBookExternalId()));
        }

        // delete
        favoriteService.removeFavorite(user.getId(), book.getId());
        FavoriteResponseDto body = favoriteMapper.toRemovedResponse(optFav.get());

        return ResponseEntity.ok(body); // 200 REMOVED
    }
    

	 // ---------------------------------------------------------------------
	 // GET /api/favorites?userEmail  -  Lister les favoris d'un utilisateur
	 // ---------------------------------------------------------------------
	 @Operation(
	     summary = "Lister les favoris d'un utilisateur",
	     description = "Retourne la liste des favoris (identifiants externes des livres) pour l'email utilisateur fourni."
	 )
	 @ApiResponses({
	     @ApiResponse(
	         responseCode = "200",
	         description = "Liste des favoris retournée",
	         content = @Content(
	             mediaType = "application/json",
	             array = @ArraySchema(schema = @Schema(implementation = FavoriteListItemDto.class)),
	             examples = @ExampleObject(
	                 name = "Exemple de liste",
	                 value = "[{\"userEmail\":\"user@example.com\",\"bookExternalId\":\"gbooks:abc123\"}," +
	                         "{\"userEmail\":\"user@example.com\",\"bookExternalId\":\"gbooks:def456\"}]"
	             )
	         )
	     ),
	     @ApiResponse(responseCode = "404", description = "Utilisateur introuvable"),
	     @ApiResponse(responseCode = "400", description = "Requête invalide (validation)")
	 })
	 @GetMapping("/favorites")
	 public ResponseEntity<List<FavoriteListItemDto>> listFavorites(
	         @Parameter(description = "Email de l'utilisateur", example = "utilisateur@example.com", required = true)
	         @RequestParam("userEmail") @jakarta.validation.constraints.Email String userEmail
	 ) {
	     Optional<User> optUser = userRepository.findByEmail(userEmail);
	     if (optUser.isEmpty()) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	     }
	
	     User user = optUser.get();
	
	     // recup fav
	     List<Favorite> favorites = favoriteService.findAllByUser_Email(user.getEmail());
	
	     // Mapping
	     List<FavoriteListItemDto> items = favorites.stream()
	             .map(favoriteMapper::toListItem) 
	             .toList();
	
	     return ResponseEntity.ok(items);
	 }

}
