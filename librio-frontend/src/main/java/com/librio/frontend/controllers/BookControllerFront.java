package com.librio.frontend.controllers;

import com.librio.frontend.dto.book.CreateBookRequestDto;
import com.librio.frontend.dto.book.LittleListBookResponseDto;
import com.librio.frontend.dto.book.BigListBookResponseDto;
import com.librio.frontend.dto.favorite.AddFavoriteRequestDto;
import com.librio.frontend.dto.favorite.FavoriteListItemDto;
import com.librio.frontend.dto.favorite.FavoriteResponseDto;
import com.librio.frontend.dto.favorite.FavoriteStatus;
import com.librio.frontend.dto.favorite.RemoveFavoriteRequestDto;
import com.librio.frontend.services.BookServiceFront;
import com.librio.frontend.services.FavoriteServiceFront;
import com.librio.frontend.services.BookServiceFront.DuplicateExternalIdException;
import com.librio.frontend.services.BookServiceFront.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/books")
public class BookControllerFront {

    private final BookServiceFront bookService;
    private final FavoriteServiceFront favoriteServiceFront;

    public BookControllerFront(BookServiceFront bookService,
                               FavoriteServiceFront favoriteServiceFront) {
        this.bookService = bookService;
        this.favoriteServiceFront = favoriteServiceFront;
    }


    @GetMapping
    public String page(
            Model model,
            @ModelAttribute("form") CreateBookForm form,
            @CookieValue(value = "userEmail", required = false) String userEmail,
            // ----- paramètres de recherche (optionnels) -----
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam(value = "categories", required = false) String categories,
            @RequestParam(value = "minRating", required = false) Long minRating
    ) {
        // acces cookie
        if (userEmail == null) {
            return "redirect:/login";
        }

        model.addAttribute("userEmail", userEmail);

        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new CreateBookForm());
        }

        boolean hasSearch =
                (title != null && !title.isBlank())
                        || (author != null && !author.isBlank())
                        || (publisher != null && !publisher.isBlank())
                        || (categories != null && !categories.isBlank())
                        || (minRating != null);

        if (hasSearch) {
            List<BigListBookResponseDto> results =
                    bookService.search(title, author, publisher, categories, minRating);
            model.addAttribute("results", results);
            model.addAttribute("searchParams", new SearchParams(title, author, publisher, categories, minRating));
        } else {
            List<LittleListBookResponseDto> books = bookService.listSummaries();
            model.addAttribute("books", books);
        }

        // Récupérer les favoris de l'utilisateur
        List<FavoriteListItemDto> favItems = favoriteServiceFront.listFavoritesByUserEmail(userEmail);
        Set<String> favoriteIds = favItems.stream()
                .filter(it -> it != null && StringUtils.hasText(it.getBookExternalId()))
                .map(FavoriteListItemDto::getBookExternalId)
                .collect(Collectors.toSet());
        model.addAttribute("favoriteIds", favoriteIds);

        return "books"; 
    }

    // Création de livre
    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") CreateBookForm form,
            BindingResult binding,
            RedirectAttributes ra
    ) {
        if (binding.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", binding);
            ra.addFlashAttribute("form", form);
            return "redirect:/ui/books";
        }

        try {
            // Mapping vers DTO back (conversion "" -> null)
            CreateBookRequestDto req = new CreateBookRequestDto();
            req.setExternalId(trimOrNull(form.getExternalId()));
            req.setTitle(trimOrNull(form.getTitle()));
            req.setSubtitle(emptyToNull(form.getSubtitle()));
            req.setAuthor(emptyToNull(form.getAuthor()));
            req.setPublisher(emptyToNull(form.getPublisher()));
            req.setPublishedDate(emptyToNull(form.getPublishedDate()));
            req.setCategories(emptyToNull(form.getCategories()));
            req.setPageCount(form.getPageCount());
            req.setAverageRating(form.getAverageRating());
            req.setImageMedium(emptyToNull(form.getImageMedium()));
            bookService.create(req);

            ra.addFlashAttribute("success", "Livre créé avec succès.");
        } catch (DuplicateExternalIdException | IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            ra.addFlashAttribute("form", form);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur inattendue lors de la création du livre.");
            ra.addFlashAttribute("form", form);
        }
        return "redirect:/ui/books";
    }

    // Suppression de livre
    @PostMapping("/{externalId}/delete")
    public String delete(@PathVariable("externalId") String externalId, RedirectAttributes ra) {
        try {
            bookService.deleteByExternalId(externalId);
            ra.addFlashAttribute("success", "Livre supprimé: " + externalId);
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur inattendue lors de la suppression.");
        }
        return "redirect:/ui/books";
    }

    // Détail complet
    @GetMapping("/{externalId}")
    public String showDetail(@PathVariable("externalId") String externalId,
                             Model model,
                             @CookieValue(value = "userEmail", required = false) String userEmail) {
        if (userEmail == null) {
            return "redirect:/login";
        }

        try {
            var book = bookService.getFull(externalId);
            model.addAttribute("book", book);
            return "book-detail";
        } catch (NotFoundException nf) {
            model.addAttribute("error", nf.getMessage());
            return "redirect:/ui/books";
        } catch (Exception ex) {
            model.addAttribute("error", "Erreur inattendue lors du chargement du livre: " + externalId);
            return "redirect:/ui/books";
        }
    }

    //add fav
    @PostMapping("/{externalId}/favorite")
    public String addFavorite(@PathVariable("externalId") String externalId,
                              @CookieValue(value = "userEmail", required = false) String userEmail,
                              RedirectAttributes ra,
                              // préserver les filtres redirect
                              @RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "author", required = false) String author,
                              @RequestParam(value = "publisher", required = false) String publisher,
                              @RequestParam(value = "categories", required = false) String categories,
                              @RequestParam(value = "minRating", required = false) Long minRating) {

        if (!StringUtils.hasText(userEmail)) {
            return "redirect:/login";
        }

        try {
            AddFavoriteRequestDto req = new AddFavoriteRequestDto();
            req.setUserEmail(userEmail);
            req.setBookExternalId(externalId);

            FavoriteResponseDto resp = favoriteServiceFront.addFavorite(req);
            if (resp != null && resp.getStatus() != null) {
                if (resp.getStatus() == FavoriteStatus.ADDED) {
                    ra.addFlashAttribute("success", "Ajouté aux favoris.");
                } else if (resp.getStatus() == FavoriteStatus.ALREADY_EXISTS) {
                    ra.addFlashAttribute("info", "Déjà en favoris.");
                } else if (resp.getStatus() == FavoriteStatus.NOT_FOUND) {
                    ra.addFlashAttribute("error", "Utilisateur ou livre introuvable.");
                } else {
                    ra.addFlashAttribute("error", "Réponse inattendue du serveur.");
                }
            } else {
                ra.addFlashAttribute("error", "Réponse inattendue du serveur.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Impossible d'ajouter aux favoris.");
        }

        return redirectWithSearchParams(title, author, publisher, categories, minRating);
    }


    @PostMapping("/{externalId}/unfavorite")
    public String removeFavorite(@PathVariable("externalId") String externalId,
                                 @CookieValue(value = "userEmail", required = false) String userEmail,
                                 RedirectAttributes ra,
                                 // préserver les filtres lors du redirect :
                                 @RequestParam(value = "title", required = false) String title,
                                 @RequestParam(value = "author", required = false) String author,
                                 @RequestParam(value = "publisher", required = false) String publisher,
                                 @RequestParam(value = "categories", required = false) String categories,
                                 @RequestParam(value = "minRating", required = false) Long minRating) {

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

        return redirectWithSearchParams(title, author, publisher, categories, minRating);
    }

    // redirection avec ancien filtres
    private static String redirectWithSearchParams(String title,
                                                   String author,
                                                   String publisher,
                                                   String categories,
                                                   Long minRating) {
        StringBuilder redirect = new StringBuilder("redirect:/ui/books");
        boolean first = true;

        first = appendQuery(redirect, first, "title", title);
        first = appendQuery(redirect, first, "author", author);
        first = appendQuery(redirect, first, "publisher", publisher);
        first = appendQuery(redirect, first, "categories", categories);

        if (minRating != null) {
            redirect.append(first ? "?" : "&").append("minRating=").append(minRating);
        }
        return redirect.toString();
    }

   
    private static boolean appendQuery(StringBuilder sb, boolean first, String key, String value) {
        if (value != null && !value.isBlank()) {
            sb.append(first ? "?" : "&").append(key).append("=")
              .append(urlEncode(value));
            return false;
        }
        return first;
    }

    private static String urlEncode(String s) {
        try {
            return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return s;
        }
    }

    // Helpers de form
    private static String trimOrNull(String s) {
        return (s == null) ? null : s.trim();
    }

    private static String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    // DTO formulaire de création
    public static class CreateBookForm {
        @NotBlank(message = "L'externalId est obligatoire.")
        private String externalId;

        @NotBlank(message = "Le titre est obligatoire.")
        private String title;

        private String subtitle;
        private String author;
        private String publisher;


        @Pattern(regexp = "^$|^\\d{2}/\\d{2}/\\d{4}$", message = "Format attendu: DD-MM-YYYY.")
		private String publishedDate;


        private String categories;
        private Integer pageCount;
        private Long averageRating;

        @URL(message = "URL d'image invalide.")
        private String imageMedium;

        public String getExternalId() { return externalId; }
        public void setExternalId(String externalId) { this.externalId = externalId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getSubtitle() { return subtitle; }
        public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getPublisher() { return publisher; }
        public void setPublisher(String publisher) { this.publisher = publisher; }
        public String getPublishedDate() { return publishedDate; }
        public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
        public String getCategories() { return categories; }
        public void setCategories(String categories) { this.categories = categories; }
        public Integer getPageCount() { return pageCount; }
        public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }
        public Long getAverageRating() { return averageRating; }
        public void setAverageRating(Long averageRating) { this.averageRating = averageRating; }
        public String getImageMedium() { return imageMedium; }
        public void setImageMedium(String imageMedium) { this.imageMedium = imageMedium; }
    }

    // Conserver les filtres saisis
    public record SearchParams(
            String title,
            String author,
            String publisher,
            String categories,
            Long minRating
    ) {}
}
