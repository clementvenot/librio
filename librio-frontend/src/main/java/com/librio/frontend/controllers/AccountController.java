package com.librio.frontend.controllers;

import com.librio.frontend.dto.favorite.FavoriteListItemDto;
import com.librio.frontend.dto.favorite.FavoriteResponseDto;
import com.librio.frontend.dto.favorite.FavoriteStatus;
import com.librio.frontend.dto.favorite.RemoveFavoriteRequestDto;
import com.librio.frontend.dto.book.LittleListBookResponseDto;
import com.librio.frontend.services.FavoriteServiceFront;
import com.librio.frontend.services.UserServiceFront;
import com.librio.frontend.services.UserServiceFront.NotFoundException;
import com.librio.frontend.services.BookServiceFront;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserServiceFront userServiceFront;
    private final FavoriteServiceFront favoriteServiceFront;
    private final BookServiceFront bookServiceFront;

    public AccountController(UserServiceFront userServiceFront,
                             FavoriteServiceFront favoriteServiceFront,
                             BookServiceFront bookServiceFront) {
        this.userServiceFront = userServiceFront;
        this.favoriteServiceFront = favoriteServiceFront;
        this.bookServiceFront = bookServiceFront;
    }

    @GetMapping
    public String account(Model model,
                          @CookieValue(value = "userEmail", required = false) String userEmail,
                          @RequestParam(value = "updated", required = false) String updated,
                          @RequestParam(value = "error", required = false) String error) {
        if (!StringUtils.hasText(userEmail)) {
            return "redirect:/login";
        }

        // Liste des fav du user
        List<FavoriteListItemDto> favorites = favoriteServiceFront.listFavoritesByUserEmail(userEmail);

        // Summaries des livres
        List<LittleListBookResponseDto> summaries = Collections.emptyList();
        try {
            summaries = bookServiceFront.listSummaries(); // GET /books
        } catch (Exception ignore) {}

        // Map <externalid,dtocomplet du livre>
        Map<String, LittleListBookResponseDto> byExternalId = summaries.stream() //transfo dto en stream
                .filter(Objects::nonNull) // on retire les null
                .collect(Collectors.toMap(
                        LittleListBookResponseDto::getExternalId,//clé
                        Function.identity(), // retourne telquel la val
                        (a, b) -> a //cas des doublons
                ));

        // Vue des fav pour thymleaf
        List<FavoriteView> favoriteViews = favorites.stream() //conversion stream
                .filter(f -> StringUtils.hasText(f.getBookExternalId())) // on garde les fav qui ont le bon extID
                .map(f -> {
                    LittleListBookResponseDto b = byExternalId.get(f.getBookExternalId());
                    FavoriteView v = new FavoriteView();
                    v.setExternalId(f.getBookExternalId()); //url
                    if (b != null) { //livre trouvé
                        v.setTitle(b.getTitle());
                        v.setAuthor(b.getAuthor());
                        v.setPublisher(b.getPublisher());
                        v.setCategories(b.getCategories());
                    } else { 
                        v.setTitle("(Livre introuvable)");
                        v.setAuthor(null);
                        v.setPublisher(null);
                        v.setCategories(null);
                    }
                    return v;
                })
                .collect(Collectors.toList()); //convert list

        //injection donnée
        model.addAttribute("email", userEmail);
        model.addAttribute("favorites", favoriteViews);
        model.addAttribute("passwordForm", new UpdatePasswordForm());
        //message flash thymleaf
        if (StringUtils.hasText(updated)) {
            model.addAttribute("successMessage", "Votre mot de passe a été mis à jour.");
        }
        if (StringUtils.hasText(error)) {
            model.addAttribute("errorMessage", error);
        }          
        return "account";
    }

    @PostMapping("/password")
    public String changePassword(@ModelAttribute("passwordForm") UpdatePasswordForm form,
                                 @CookieValue(value = "userEmail", required = false) String userEmail,
                                 RedirectAttributes ra) {
        
    	//a faire avec spring secu si j'ai le temps
    	if (!StringUtils.hasText(userEmail)) {
            return "redirect:/login";
        }
        
        // verif en front que les mdp soit les meme pour le modifier
        if (!StringUtils.hasText(form.getNewPassword()) || form.getNewPassword().length() < 5 || form.getNewPassword().length() > 128) {
            ra.addAttribute("error", "Le mot de passe doit contenir entre 5 et 128 caractères.");
            return "redirect:/account";
        }
        if (!Objects.equals(form.getNewPassword(), form.getConfirmNewPassword())) {
            ra.addAttribute("error", "Les deux mots de passe ne correspondent pas.");
            return "redirect:/account";
        }

        //modification du password
        try {
            userServiceFront.updatePassword(userEmail, form.getNewPassword());
            ra.addAttribute("updated", "1");
            return "redirect:/account";
        } catch (NotFoundException nf) {
            ra.addAttribute("error", nf.getMessage());
            return "redirect:/account";
        } catch (IllegalArgumentException iae) {
            ra.addAttribute("error", iae.getMessage());
            return "redirect:/account";
        } catch (Exception ex) {
            ra.addAttribute("error", "Erreur lors de la mise à jour du mot de passe.");
            return "redirect:/account";
        }
    }
    
    // suppression fav 
    @PostMapping("/{externalId}/unfavorite")
    public String removeFavorite(@PathVariable("externalId") String externalId,
                                 @CookieValue(value = "userEmail", required = false) String userEmail,
                                 RedirectAttributes ra) {

        if (!StringUtils.hasText(userEmail)) {
            return "redirect:/login";
        }

        try {
            RemoveFavoriteRequestDto req = new RemoveFavoriteRequestDto();
            req.setUserEmail(userEmail);
            req.setBookExternalId(externalId);

            FavoriteResponseDto resp = favoriteServiceFront.removeFavorite(req);
            if (resp != null && resp.getStatus() != null) {
                if (resp.getStatus() == FavoriteStatus.REMOVED) {
                    ra.addFlashAttribute("success", "Retiré des favoris.");
                } else if (resp.getStatus() == FavoriteStatus.NOT_FOUND) {
                    ra.addFlashAttribute("error", "Favori, utilisateur ou livre introuvable.");
                } else {
                    ra.addFlashAttribute("error", "Réponse inattendue du serveur.");
                }
            } else {
                ra.addFlashAttribute("error", "Réponse inattendue du serveur.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Impossible de retirer des favoris.");
        }

        return "redirect:/account";
    }

    //deconnexion (suppression cookie puis redirection)
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("userEmail", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        response.addCookie(cookie);
        return "redirect:/login";
    }

    /** model pour affichage des favoris */
    public static class FavoriteView {
        private String externalId;   
        private String title;
        private String author;
        private String publisher;
        private String categories;

        public String getExternalId() { 
        	return externalId; 
        	}
        public void setExternalId(String externalId) { 
        	this.externalId = externalId; 
        	}
        public String getTitle() { 
        	return title; 
        	}
        public void setTitle(String title) { 
        	this.title = title; 
        	}
        public String getAuthor() { 
        	return author; 
        	}
        public void setAuthor(String author) { 
        	this.author = author; 
        	}
        public String getPublisher() { 
        	return publisher; 
        	}
        public void setPublisher(String publisher) { 
        	this.publisher = publisher; 
        	}
        public String getCategories() { 
        	return categories; 
        	}
        public void setCategories(String categories) { 
        	this.categories = categories; 
        	}
    }

    public static class UpdatePasswordForm {
        private String newPassword;
        private String confirmNewPassword;
        public String getNewPassword() { 
        	return newPassword; 
        	}
        public void setNewPassword(String newPassword) { 
        	this.newPassword = newPassword; 
        	}
        public String getConfirmNewPassword() { 
        	return confirmNewPassword; 
        	}
        public void setConfirmNewPassword(String confirmNewPassword) { 
        	this.confirmNewPassword = confirmNewPassword; 
        	}
    }
}
