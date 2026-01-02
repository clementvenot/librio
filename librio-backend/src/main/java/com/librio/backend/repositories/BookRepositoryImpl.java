package com.librio.backend.repositories;

import com.librio.backend.entities.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> searchBooks(String title, String author, String publisher, String categories, Long minRating) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);

        Root<Book> root = cq.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        // LIKE 
        if (StringUtils.hasText(title)) {
            String pattern = "%" + title.trim().toUpperCase() + "%";
            predicates.add(cb.like(cb.upper(root.get("title")), pattern));
        }
        if (StringUtils.hasText(author)) {
            String pattern = "%" + author.trim().toUpperCase() + "%";
            predicates.add(cb.like(cb.upper(root.get("author")), pattern));
        }
        if (StringUtils.hasText(publisher)) {
            String pattern = "%" + publisher.trim().toUpperCase() + "%";
            predicates.add(cb.like(cb.upper(root.get("publisher")), pattern));
        }
        if (StringUtils.hasText(categories)) {
            String pattern = "%" + categories.trim().toUpperCase() + "%";
            predicates.add(cb.like(cb.upper(root.get("categories")), pattern));
        }

        // Filtre note minimale
        if (minRating != null) {
            Path<Long> ratingPath = root.get("averageRating");

            predicates.add(cb.isNotNull(ratingPath)); //exclu livres avec notes null
            predicates.add(cb.greaterThanOrEqualTo(ratingPath, minRating));
        }

        // pas de paramètre -> pas de filtre
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        // Tri par titre puis auteur
        cq.orderBy(cb.asc(root.get("title")), cb.asc(root.get("author")));

        TypedQuery<Book> query = em.createQuery(cq);
        return query.getResultList();
    }
}
