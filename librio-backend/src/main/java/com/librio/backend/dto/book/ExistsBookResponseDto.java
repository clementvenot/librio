package com.librio.backend.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "ExistsBookResponseDto",
    description = "Réponse indiquant si un livre existe déjà dans la base."
)
public class ExistsBookResponseDto {

    @NotBlank
    @Size(max = 100)
    @Schema(
        description = "Identifiant externe du livre (ex: Google Books ID).",
        example = "gBoDDAAAQBAJ",
        maxLength = 100
    )
    private String externalId;

    @Schema(
        description = "Indique si le livre existe dans la base.",
        example = "true"
    )
    private boolean exists;

    public ExistsBookResponseDto() {}

    public ExistsBookResponseDto(String externalId, boolean exists) {
        this.externalId = externalId;
        this.exists = exists;
    }

    public String getExternalId() { 
    	return externalId; 
    	}
    public void setExternalId(String externalId) { 
    	this.externalId = externalId; 
    	}

    public boolean isExists() { 
    	return exists; 
    	}
    public void setExists(boolean exists) { 
    	this.exists = exists; 
    	}
}

