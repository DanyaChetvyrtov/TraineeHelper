package ru.core.profilems.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Exception response")
public class ExceptionResponse {
    @Schema(description = "Status code", example = "400", type = "int")
    private int status;
    @Schema(description = "Error", example = "Bad request", type = "String")
    private String error;
    @Schema(description = "Path produced error", example = "api/v1/task", type = "String")
    private String path;
    @Schema(description = "Detailed message", example = "Validation failed", type = "String")
    private String message;
    @Schema(description = "Timestamp", example = "2025-06-23T13:10:29.7871413", type = "Timestamp")
    private LocalDateTime timestamp;
    @Schema(description = "List of errors", type = "Map<String, String>")
    private Map<String, String> errors;

}