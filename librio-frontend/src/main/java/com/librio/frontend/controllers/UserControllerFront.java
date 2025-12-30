package com.librio.frontend.controllers;

import com.librio.frontend.dto.CreateUserResponseDto;
import com.librio.frontend.dto.LoginResponseDto;
import com.librio.frontend.services.UserServiceFront;
import com.librio.frontend.services.UserServiceFront.EmailAlreadyUsedException;
import com.librio.frontend.services.UserServiceFront.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserControllerFront {

    private final UserServiceFront client;

    public UserControllerFront(UserServiceFront client) {
        this.client = client;
    }

    //initialisation form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("email", "");
        model.addAttribute("password", "");
        model.addAttribute("error", null);
        model.addAttribute("success", null);
        return "register";
    }

    //create user
    @PostMapping("/register")
    public String submitRegister(@RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 Model model) {
        try {
            var existsResp = client.checkExists(email); //verif du back
            if (existsResp != null && existsResp.isExists()) {
                model.addAttribute("error", "Cet email est déjà utilisé.");
                model.addAttribute("email", email);
                return "register";
            }

            CreateUserResponseDto created = client.createUser(email, password);
            model.addAttribute("success", created.getMessage());
            return "redirect:/login?created";
        } catch (EmailAlreadyUsedException e) { //onStatus 409
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) { //erreurs génériques comme timeout
            model.addAttribute("error", "Erreur lors de la création du compte.");
            return "register";
        }
    }

    //initialisation connect
    @GetMapping("/login")
    public String showLoginForm(Model model,
                                @RequestParam(value="created", required=false) String created) {
        if (created != null) {
            model.addAttribute("info", "Compte créé. Vous pouvez vous connecter.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String submitLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpServletResponse response,
                               Model model) {
        try {
            LoginResponseDto resp = client.login(email, password);
            if (resp.isAuthenticated()) {
                // Créer un cookie avec l'email
                Cookie cookie = new Cookie("userEmail", email);
                cookie.setPath("/");  // valable pour toute l'app
                cookie.setHttpOnly(false);  
                cookie.setSecure(false);   
                cookie.setMaxAge(60 * 60);  // 1 heure (en secondes)
                response.addCookie(cookie);

                return "redirect:/home";
            } else {
                model.addAttribute("error", resp.getMessage()); //on Status 400
                return "login";
            }
        } catch (UnauthorizedException e) { //onStatus 401
            model.addAttribute("error", e.getMessage());
            return "login";
        } catch (Exception e) { // cas générique onStatus 400
            model.addAttribute("error", "Erreur de connexion.");
            return "login";
        }
    }

    @GetMapping("/home")
    public String home(Model model,
                       @CookieValue(value = "userEmail", required = false) String userEmail) {
    	// ----------------------------------------------------------------------------------
    	// Normalement avec spring boot pour une meilleure secu (je le ferai si j'ai le temps)
    	if (userEmail == null) {
    		return "redirect:/login"; } 
    	// ----------------------------------------------------------------------------------
    	
        model.addAttribute("email", userEmail != null ? userEmail : userEmail);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Supprimer le cookie
        Cookie cookie = new Cookie("userEmail", "");
        cookie.setPath("/");
        cookie.setMaxAge(0); // suppression immédiate
        response.addCookie(cookie);

        return "redirect:/login";
    }
}
