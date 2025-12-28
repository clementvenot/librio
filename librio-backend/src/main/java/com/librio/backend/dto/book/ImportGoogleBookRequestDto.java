package com.librio.backend.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ImportGoogleBookRequestDto {

    @NotBlank
    @Size(max = 100)
    private String googleVolumeId;

    public ImportGoogleBookRequestDto() {}

    public String getGoogleVolumeId() { return googleVolumeId; }
    public void setGoogleVolumeId(String googleVolumeId) { this.googleVolumeId = googleVolumeId; }
}
