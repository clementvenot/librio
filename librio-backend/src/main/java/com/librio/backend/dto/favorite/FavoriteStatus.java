package com.librio.backend.dto.favorite;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Statut du favori")
public enum FavoriteStatus {

    @Schema(description = "Favori ajouté avec succès")
    ADDED,

    @Schema(description = "Le favori existe déjà pour cet utilisateur")
    ALREADY_EXISTS,

    @Schema(description = "Favori supprimé avec succès")
    REMOVED,

    @Schema(description = "Aucun favori trouvé pour ce couple user/book")
    NOT_FOUND
}
