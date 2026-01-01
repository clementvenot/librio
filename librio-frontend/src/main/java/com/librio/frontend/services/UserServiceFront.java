package com.librio.frontend.services;

import com.librio.frontend.dto.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserServiceFront {

    private final RestClient restClient;

    public UserServiceFront(RestClient restClient) {
        this.restClient = restClient;
    }
    
    // reactions aux reponses du back 
    public UserExistResponseDto checkExists(String email) {
        UserExistRequestDto req = new UserExistRequestDto();
        req.setEmail(email);

        return restClient.post()
                .uri("/users/exists")
                .contentType(MediaType.APPLICATION_JSON)
                .body(req)
                .retrieve()
                .onStatus(status -> status.value() == 400, (reqSpec, res) -> { // si la reponse du back est 400                
                	throw new IllegalArgumentException("Requête invalide: email incorrect ou manquant.");
                })
                .body(UserExistResponseDto.class);
    }

    public CreateUserResponseDto createUser(String email, String password) {
        CreateUserRequestDto req = new CreateUserRequestDto();
        req.setEmail(email);
        req.setPassword(password);

        return restClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(req)
                .retrieve()
                .onStatus(status -> status.value() == 409, (reqSpec, res) -> { // si la reponse du back est 409 
                    throw new EmailAlreadyUsedException("Cet email est déjà utilisé.");
                })
                .body(CreateUserResponseDto.class);
    }

    public LoginResponseDto login(String email, String password) {
        LoginRequestDto req = new LoginRequestDto();
        req.setEmail(email);
        req.setPassword(password);

        return restClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(req)
                .retrieve()
                .onStatus(status -> status.value() == 401, (reqSpec, res) -> { // si la rep du back est 401
                    throw new UnauthorizedException("Email ou mot de passe incorrect.");
                })
                .body(LoginResponseDto.class);
    }
    
    
     public UpdatePasswordResponseDto updatePassword(String email, String newPassword) {
         UpdatePasswordRequestDto req = new UpdatePasswordRequestDto();
         req.setEmail(email);
         req.setNewPassword(newPassword);

         return restClient.put()
                 .uri("/users/password")
                 .contentType(MediaType.APPLICATION_JSON)
                 .body(req)
                 .retrieve()
                 .onStatus(status -> status.value() == 404, (spec, res) -> { // reponse 404 ot found
                     throw new NotFoundException("Utilisateur introuvable.");
                 })
                 .onStatus(status -> status.value() == 400, (spec, res) -> { // request invalide 400
                     throw new IllegalArgumentException("Requête invalide (validation).");
                 })
                 .body(UpdatePasswordResponseDto.class); // 200 reussis
     }

     // exceptions personalisées :
     public static class NotFoundException extends RuntimeException {
         private static final long serialVersionUID = 1L;
         public NotFoundException(String msg) { super(msg); }
     }
 
    public static class EmailAlreadyUsedException extends RuntimeException {
        private static final long serialVersionUID = 1L;
		public EmailAlreadyUsedException(String msg) { super(msg); }
    }

    public static class UnauthorizedException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public UnauthorizedException(String msg) { super(msg); }
    }
}
