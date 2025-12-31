package com.librio.backend.controllers;

import com.librio.backend.dto.user.CreateUserRequestDto;
import com.librio.backend.dto.user.CreateUserResponseDto;
import com.librio.backend.dto.user.LoginRequestDto;
import com.librio.backend.dto.user.LoginResponseDto;
import com.librio.backend.dto.user.UserExistRequestDto;
import com.librio.backend.dto.user.UserExistResponseDto;
import com.librio.backend.entities.User;
import com.librio.backend.mappers.UserMapper;
import com.librio.backend.services.IUserService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Users", description = "Endpoints liés aux utilisateurs (existence, création, login)")
public class UserController {

    private final IUserService userService;
    private final UserMapper userMapper;

    public UserController(IUserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // ------------------------------------------
    // POST /api/users/exists  -  Email Exists ?
    // ------------------------------------------
    @Operation(
        summary = "Vérifier l'existence d'un utilisateur par email",
        description = "Renvoie un booléen indiquant si l'email est déjà enregistré."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserExistResponseDto.class), // 200 UserExistResponseDto
                examples = {
                    @ExampleObject(
                        name = "Existe",
                        value = "{\"email\":\"user@example.com\",\"exists\":true}"),
                    @ExampleObject(
                        name = "N'existe pas",
                        value = "{\"email\":\"new@example.com\",\"exists\":false}")
                }
            )
        ),
        @ApiResponse(responseCode = "400", 
        			 description = "Requête invalide (validation)")
    })
    @PostMapping("/users/exists")
    public ResponseEntity<UserExistResponseDto> existsUser(
            @Valid @RequestBody UserExistRequestDto dto) {
        boolean exists = userService.existsByEmail(dto.getEmail()); 
        UserExistResponseDto resp = userMapper.toUserExistResponse(dto.getEmail(), exists); // 200 UserExistResponseDto
        return ResponseEntity.ok(resp);
    }

    // ------------------------------------------
    // POST /api/users  -  Création de user 
    // ------------------------------------------
	@Operation(
	    summary = "Créer un utilisateur",
	    description = "Crée un compte si l'email n'existe pas déjà. Retourne 201 Created avec Location."
	)
	@ApiResponses({
	    @ApiResponse(
	        responseCode = "201",
	        description = "Utilisateur créé",
	        content = @Content(
	            mediaType = "application/json",
	            schema = @Schema(implementation = CreateUserResponseDto.class), // 201 CreateUserResponseDto
	            examples = @ExampleObject(
	                name = "Création réussie",
	                value = "{\"id\":101,\"email\":\"new@example.com\",\"message\":\"Compte créé avec succès\"}"
	            )
	        )
	    ),
	    @ApiResponse(
	        responseCode = "409",
	        description = "Conflit: email déjà utilisé",
	        content = @Content(
	            mediaType = "application/json",
	            schema = @Schema(implementation = UserExistResponseDto.class), // 409 UserExistResponseDto
	            examples = @ExampleObject(
	                name = "Email existant",
	                value = "{\"email\":\"user@example.com\",\"exists\":true}"
	            )
	        )
	    ),
	    @ApiResponse(responseCode = "400", description = "Requête invalide (validation)")
	})
	@PostMapping("/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDto dto) {
	    if (userService.existsByEmail(dto.getEmail())) {
	        UserExistResponseDto conflict = userMapper.toUserExistResponse(dto.getEmail(), true);
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(conflict);  // 409 UserExistResponseDto
	    }
	
	    User created = userService.register(dto.getEmail(), dto.getPassword());
	    URI location = URI.create("/api/users/" + created.getId());
	
	    CreateUserResponseDto body = new CreateUserResponseDto(  // 201 CreateUserResponseDto
	            created.getId(),
	            created.getEmail(),
	            "Compte créé avec succès"
	    );
	
	    return ResponseEntity.created(location).body(body);
	}

    // ------------------------------------------
    // POST /api/auth/login  -  authentification user 
    // ------------------------------------------
    @Operation(
        summary = "Connexion utilisateur",
        description = "Vérifie email et mot de passe et renvoie l'état d'authentification."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Connexion réussie",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponseDto.class), // 200 LoginResponseDto
                examples = {
                    @ExampleObject(
                        name = "Succès",
                        value = "{\"authenticated\":true,\"message\":\"Connexion réussie\"}")
                }
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Échec d'authentification",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponseDto.class), // 401 LoginResponseDto
                examples = {
                    @ExampleObject(
                        name = "Échec",
                        value = "{\"authenticated\":false,\"message\":\"Email ou mot de passe incorrect\"}")
                }
            )
        ),
        @ApiResponse(responseCode = "400", description = "Requête invalide (validation)")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> connectUser(
            @Valid @RequestBody LoginRequestDto dto) {

        Optional<User> opt = userService.findByEmail(dto.getEmail());
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(userMapper.toLoginResponse(false, "Email ou mot de passe incorrect")); // 401 LoginResponseDto
        }

        User user = opt.get();
        boolean ok = user.getPassword().equals(dto.getPassword()); 

        if (!ok) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(userMapper.toLoginResponse(false, "Email ou mot de passe incorrect")); // 401 LoginResponseDto
        }

        return ResponseEntity.ok(userMapper.toLoginResponse(true, "Connexion réussie")); // 200 LoginResponseDto
    }
}
