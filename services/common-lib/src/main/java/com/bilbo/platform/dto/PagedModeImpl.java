package com.bilbo.platform.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;

import java.util.List;

public class PagedModeImpl<T> extends PagedModel<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PagedModeImpl(@JsonProperty("content") List<T> content,
                         @JsonProperty("page") Page page) {
        super(new PageImpl<>(content, PageRequest.of(page.number, page.size), page.totalElements));
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Page {
        private int size;
        private int number;
        private long totalElements;
    }
}