package ru.core.profilems.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
@Builder
public class SearchParameters {
    private String query;
    private boolean ignoreCase;
    private Integer page;
    private Integer size;

    public PageRequest getPageRequest() {
        return PageRequest.of(page - 1, size);
    }
}
