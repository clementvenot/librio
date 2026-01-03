package com.librio.backend.controllers;

import com.librio.backend.dto.book.BigListBookResponseDto;
import com.librio.backend.dto.book.CreateBookRequestDto;
import com.librio.backend.dto.book.LittleListBookResponseDto;
import com.librio.backend.dto.book.ErrorResponseDto;
import com.librio.backend.dto.book.ExistsBookResponseDto;
import com.librio.backend.entities.Book;
import com.librio.backend.mappers.BookMapper;
import com.librio.backend.services.IBookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Endpoints liés aux livres (création, suppression, récupération de données)")
public class BookController {

    private final IBookService bookService;
    private final BookMapper bookMapper;

    public BookController(IBookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    // ------------------------------------------
    // POST /api/books  -  Création
    // ------------------------------------------
    @Operation(
        summary = "Créer un livre",
        description = "Crée un livre à partir des données reçues. (Évolution prévue : n'accepter que l'externalId et enrichir via Google Books.)"
        )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Livre créé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BigListBookResponseDto.class),
                examples = {
                    @ExampleObject(
                        name = "Succès",
                        value = """
                                {
                                  "externalId":"gbooks:abc123",
                                  "title":"Clean Architecture",
                                  "subtitle":"A Craftsman's Guide",
                                  "author":"Robert C. Martin",
                                  "publisher":"Prentice Hall",
                                  "publishedDate":"2017-09-20",
                                  "categories":"Software Engineering",
                                  "pageCount":432,
                                  "averageRating":5,
                                  "imageMedium":"https://example.com/cover.jpg"
                                }
                                """)})),
        @ApiResponse(
            responseCode = "409",
            description = "externalId déjà utilisé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExistsBookResponseDto.class),
                examples = {
                    @ExampleObject(
                        name = "Conflit",
                        value = "{\"externalId\":\"gbooks:abc123\",\"exists\":true}")})),
        @ApiResponse(
            responseCode = "400",
            description = "Requête invalide (validation)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = {
                    @ExampleObject(
                        name = "Validation KO",
                        value = "{\"error\":\"Bad Request\",\"validationErrors\":{\"externalId\":\"must not be blank\",\"title\":\"must not be blank\"}}")}))
        })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateBookRequestDto req) {
        if (bookService.existsByExternalId(req.getExternalId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ExistsBookResponseDto(req.getExternalId(), true));
        }
        Book created = bookService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.toBigListDto(created));
    }

    // ------------------------------------------
    // GET /api/books  -  Listing resumé de tout les livres 
    // ------------------------------------------
    @Operation(
        summary = "Lister les livres",
        description = "Retourne la liste des livres au format condensé (résumé)."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Liste des livres",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = LittleListBookResponseDto.class)),
                examples = {
                    @ExampleObject(
                        name = "Liste",
                        value = """
                                [
                                  {
                                    "title":"Clean Architecture",
                                    "author":"Robert C. Martin",
                                    "publisher":"Prentice Hall",
                                    "categories":"Software Engineering",
                                    "externalId":"gbooks:abc123"
                                  },
                                  {
                                    "title":"Domain-Driven Design",
                                    "author":"Eric Evans",
                                    "publisher":"Addison-Wesley",
                                    "categories":"Software Architecture",
                                    "externalId":"gbooks:def456"
                                  }
                                ]
                                """)}))
        })
    @GetMapping
    public ResponseEntity<List<LittleListBookResponseDto>> list() {
        List<Book> entities = bookService.findAll();
        return ResponseEntity.ok(bookMapper.toLittleListDtos(entities));
    }

    // ------------------------------------------
    // GET /api/books/{externalId}/exists  -  ExternalID Exists ? 
    // ------------------------------------------
    @Operation(
        summary = "Vérifier l'existence d'un livre",
        description = "Retourne si un livre avec cet externalId existe déjà."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Résultat d'existence",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExistsBookResponseDto.class),
                examples = {
                    @ExampleObject(name = "Existe", value = "{\"externalId\":\"gbooks:abc123\",\"exists\":true}"),
                    @ExampleObject(name = "N'existe pas", value = "{\"externalId\":\"gbooks:zzz999\",\"exists\":false}")}))
        })
    @GetMapping("/{externalId}/exists")
    public ResponseEntity<ExistsBookResponseDto> exists(
            @Parameter(description = "Identifiant externe de googleBook", required = true)
            @PathVariable("externalId") String externalId) {
        boolean exists = bookService.existsByExternalId(externalId);
        return ResponseEntity.ok(new ExistsBookResponseDto(externalId, exists));
    }

    // ------------------------------------------
    // GET /api/books/{externalId}/full  -  Détails complets par externalID
    // ------------------------------------------
    @Operation(
        summary = "Récupérer les informations détaillées d’un livre",
        description = "Retourne toutes les informations d’un livre par externalId."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Livre trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BigListBookResponseDto.class),
                examples = @ExampleObject(
                    name = "Détail complet",
                    value = """
                            {
                              "externalId":"gbooks:abc123",
                              "title":"Clean Architecture",
                              "subtitle":"A Craftsman's Guide",
                              "author":"Robert C. Martin",
                              "publisher":"Prentice Hall",
                              "publishedDate":"2017-09-20",
                              "categories":"Software Engineering",
                              "pageCount":432,
                              "averageRating":5,
                              "imageMedium":"https://example.com/cover.jpg"
                            }
                            """))),
        @ApiResponse(
            responseCode = "404",
            description = "Livre non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(
                    name = "Not Found",
                    value = "{\"error\":\"Not Found\",\"message\":\"Aucun livre avec externalId: gbooks:xyz999\"}")))
        })
    @GetMapping("/{externalId}/full")
    public ResponseEntity<?> getAllInfo(
            @Parameter(description = "Identifiant externe (ISBN ou volumeId Google)", required = true)
            @PathVariable("externalId") String externalId) {

        if (!bookService.existsByExternalId(externalId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto("Not Found", "Aucun livre avec externalId: " + externalId));
        }
        return bookService.findByExternalId(externalId)
                .map(bookMapper::toBigListDto)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDto("Not Found", "Aucun livre avec externalId: " + externalId)));
    }

    // ------------------------------------------
    // GET /api/books/{externalId}/summary  -  Détails résumés par externalID
    // ------------------------------------------
    @Operation(
        summary = "Récupérer des informations résumées d’un livre",
        description = "Retourne titre, auteur, éditeur et catégories par externalId."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Livre trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LittleListBookResponseDto.class),
                examples = @ExampleObject(
                    name = "Résumé",
                    value = """
                            {
                              "title":"Clean Architecture",
                              "author":"Robert C. Martin",
                              "publisher":"Prentice Hall",
                              "categories":"Software Engineering",
                              "externalId":"gbooks:abc123"
                            }
                            """))),
        @ApiResponse(
            responseCode = "404",
            description = "Livre non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(
                    name = "Not Found",
                    value = "{\"error\":\"Not Found\",\"message\":\"Aucun livre avec externalId: gbooks:xyz999\"}")))
        })
    @GetMapping("/{externalId}/summary")
    public ResponseEntity<?> getFewInfo(
            @Parameter(description = "Identifiant externe (ISBN ou volumeId Google)", required = true)
            @PathVariable("externalId") String externalId) {

        if (!bookService.existsByExternalId(externalId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto("Not Found", "Aucun livre avec externalId: " + externalId));
        }
        return bookService.findByExternalId(externalId)
                .map(bookMapper::toLittleListDto)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDto("Not Found", "Aucun livre avec externalId: " + externalId)));
    }

    // ------------------------------------------
    // DELETE /api/books/{externalId}  -  Suppression par externalID
    // ------------------------------------------
    @Operation(
        summary = "Supprimer un livre",
        description = "Supprime un livre par externalId."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Supprimé",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(name = "No Content", value = ""))),
        @ApiResponse(
            responseCode = "404",
            description = "Livre non trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(
                    name = "Not Found",
                    value = "{\"error\":\"Not Found\",\"message\":\"Aucun livre avec externalId: gbooks:xyz999\"}")))
        })
    @DeleteMapping("/{externalId}")
    public ResponseEntity<?> deleteByExternalId(
            @Parameter(description = "Identifiant externe du livre à supprimer", required = true)
            @PathVariable("externalId") String externalId) {

        if (!bookService.existsByExternalId(externalId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto("Not Found", "Aucun livre avec externalId: " + externalId));
        }
        bookService.deleteByExternalId(externalId);
        return ResponseEntity.noContent().build();
    }
    
	@Operation(
	    summary = "Rechercher des livres",
	    description = "Recherche par title, author, publisher, categories avec Like, "
	                + "avec filtre sur la note minimale averageRating >= minRating."
	)
	@GetMapping("/search")
	public ResponseEntity<List<BigListBookResponseDto>> search(
	        @Parameter(description = "Filtre sur le titre (LIKE, case-insensitive)", example = "Clean")
	        @RequestParam(value = "title", required = false) String title,
	        @Parameter(description = "Filtre sur l'auteur (LIKE, case-insensitive)", example = "Martin")
	        @RequestParam(value = "author", required = false) String author,
	        @Parameter(description = "Filtre sur l'éditeur (LIKE, case-insensitive)", example = "Prentice")
	        @RequestParam(value = "publisher", required = false) String publisher,
	        @Parameter(description = "Filtre sur les catégories (LIKE, case-insensitive)", example = "Software")
	        @RequestParam(value = "categories", required = false) String categories,
	        @Parameter(description = "Note minimale (>=). Les livres sans note sont ignorés si ce paramètre est fourni.", example = "4")
	        @RequestParam(value = "minRating", required = false) Long minRating
	) {
	    List<Book> results = bookService.search(title, author, publisher, categories, minRating);
	    return ResponseEntity.ok(bookMapper.toBigListDtos(results));
	}

}
