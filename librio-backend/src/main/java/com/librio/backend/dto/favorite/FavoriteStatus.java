package com.librio.backend.dto.favorite;

public enum FavoriteStatus {
    ADDED,           // favori ajouté
    ALREADY_EXISTS,  // le couple user/book était déjà en favoris
    REMOVED,         // favori supprimé
    NOT_FOUND        // aucun favori trouvé 
}
