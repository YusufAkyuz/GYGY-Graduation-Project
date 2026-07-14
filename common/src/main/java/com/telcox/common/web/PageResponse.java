package com.telcox.common.web;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Spring Data Page'i servisler arası kararlı bir sözleşmeye çeviren zarf (TR-18).
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
