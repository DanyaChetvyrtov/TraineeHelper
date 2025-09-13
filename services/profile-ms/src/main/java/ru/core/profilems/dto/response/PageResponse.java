package ru.core.profilems.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({
        "content", "totalPages", "totalElements",
        "curPage", "pageSize"
})
@Schema(description = "Page response")
public class PageResponse<T> {

    @Schema(description = "Page content. List of objects", type = "List")
    private List<T> content;

    @Schema(description = "Total pages amount", example = "5", type = "Integer")
    private int totalPages;

    @Schema(description = "Total elements amount", example = "25", type = "Long")
    private long totalElements;

    @Schema(description = "Current page number", example = "3", type = "Integer")
    private int curPage;

    @Schema(description = "Amount of elements for each page", example = "5", type = "Integer")
    private int pageSize;
}
