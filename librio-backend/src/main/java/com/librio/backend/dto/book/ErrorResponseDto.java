
package com.librio.backend.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "ErrorResponseDto",
    description = "Structure standard pour représenter une erreur dans l'API."
)
public class ErrorResponseDto {

    @Schema(
        description = "Code ou type d'erreur.",
        example = "BAD_REQUEST"
    )
    private String error;

    @Schema(
        description = "Message détaillé expliquant la cause de l'erreur.",
        example = "Le champ 'title' est obligatoire."
    )
    private String message;

    public ErrorResponseDto() {}

    public ErrorResponseDto(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
