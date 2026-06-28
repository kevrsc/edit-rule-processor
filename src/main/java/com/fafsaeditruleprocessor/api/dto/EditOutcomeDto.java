package com.fafsaeditruleprocessor.api.dto;

import org.jspecify.annotations.Nullable;

public record EditOutcomeDto(
        String id, String name, boolean passed, String severity, @Nullable String message) {
}
