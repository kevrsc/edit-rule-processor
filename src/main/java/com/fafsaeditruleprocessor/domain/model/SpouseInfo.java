package com.fafsaeditruleprocessor.domain.model;

import org.jspecify.annotations.Nullable;

public record SpouseInfo(
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String ssn) {
}
