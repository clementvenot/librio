package com.librio.backend.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ExistsBookResponseDto {

    @NotBlank
    @Size(max = 100)
    private String externalId;

    private boolean exists;

    public ExistsBookResponseDto() {}

    public ExistsBookResponseDto(String externalId, boolean exists) {
        this.externalId = externalId;
        this.exists = exists;
    }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    public boolean isExists() { return exists; }
    public void setExists(boolean exists) { this.exists = exists; }
}
