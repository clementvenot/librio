package com.librio.frontend.controllers;

import com.librio.frontend.dto.book.CreateBookRequestDto;
import com.librio.frontend.dto.book.LittleListBookResponseDto;
import com.librio.frontend.services.BookServiceFront;
import com.librio.frontend.services.BookServiceFront.DuplicateExternalIdException;
import com.librio.frontend.services.BookServiceFront.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ui/books")
public class BookControllerFront {

    private final BookServiceFront bookService;

    public BookControllerFront(BookServiceFront bookService) {
        this.bookService = bookService;
    }

    // affiche formulaire + liste 
    @GetMapping
    public String page(Model model, 
    		@ModelAttribute("form") CreateBookForm form, 
    		@CookieValue(value = "userEmail", required = false) String userEmail) {
    	// ----------------------------------------------------------------------------------
    	// Normalement avec spring boot pour une meilleure secu (je le ferai si j'ai le temps)
    	if (userEmail == null) {
    		return "redirect:/login"; } 
    	// ----------------------------------------------------------------------------------
    	
    	if (!model.containsAttribute("form")) {
            model.addAttribute("form", new CreateBookForm());
        }

        List<LittleListBookResponseDto> books = bookService.listSummaries();
        model.addAttribute("books", books);
        return "books"; //books.html
    }

    // traite le formulaire 
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
            // Mapping vers DTO back 
        	//on convertit "" en null 
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
        } catch (Exception e) { //message generique
            ra.addFlashAttribute("error", "Erreur inattendue lors de la création du livre.");
            ra.addFlashAttribute("form", form);
        }
        return "redirect:/ui/books";
    }

    // SUPPRESSION
    @PostMapping("/{externalId}/delete")
    public String delete(@PathVariable("externalId") String externalId, RedirectAttributes ra) {
        try {
            bookService.deleteByExternalId(externalId);
            ra.addFlashAttribute("success", "Livre supprimé: " + externalId);
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) { //message générique
            ra.addFlashAttribute("error", "Erreur inattendue lors de la suppression.");
        }
        return "redirect:/ui/books";
    }

    // obligatoire dans le form
    private static String trimOrNull(String s) {
        return (s == null) ? null : s.trim();
    }
    
    //conversion "" to null
    private static String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    // DTO formulaire books
    public static class CreateBookForm {
        @NotBlank(message = "L'externalId est obligatoire.")
        private String externalId;
        @NotBlank(message = "Le titre est obligatoire.")
        private String title;

        private String subtitle;
        private String author;
        private String publisher;
        @Pattern(regexp = "^$|^\\d{4}-\\d{2}-\\d{2}$", message = "Format attendu: YYYY-MM-DD.")
        private String publishedDate;
        private String categories;
        private Integer pageCount;
        private Long averageRating; 
        @URL(message = "URL d'image invalide.")
        private String imageMedium;
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
		public String getSubtitle() {
			return subtitle;
		}
		public void setSubtitle(String subtitle) {
			this.subtitle = subtitle;
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
		public String getPublishedDate() {
			return publishedDate;
		}
		public void setPublishedDate(String publishedDate) {
			this.publishedDate = publishedDate;
		}
		public String getCategories() {
			return categories;
		}
		public void setCategories(String categories) {
			this.categories = categories;
		}
		public Integer getPageCount() {
			return pageCount;
		}
		public void setPageCount(Integer pageCount) {
			this.pageCount = pageCount;
		}
		public Long getAverageRating() {
			return averageRating;
		}
		public void setAverageRating(Long averageRating) {
			this.averageRating = averageRating;
		}
		public String getImageMedium() {
			return imageMedium;
		}
		public void setImageMedium(String imageMedium) {
			this.imageMedium = imageMedium;
		}


    }
}
